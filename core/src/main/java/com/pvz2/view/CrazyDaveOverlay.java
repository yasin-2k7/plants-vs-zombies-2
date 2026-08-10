package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.pvz2.Main;
import com.pvz2.models.world.GameWorld;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;

public class CrazyDaveOverlay extends WidgetGroup {
    private Runnable onCompleteAction;
    private final Main game;
    private final GameWorld world;
    private final PamPlayer davePam;
    private final List<String> dialogs;

    private int currentDialogIndex = 0;
    private boolean isStarted = false;
    private boolean finished = false;
    private boolean isLeaving = false;

    private float stateTime = 0f;
    private String currentAnim = "anim_enter";

    private final Image bubbleImage;
    private final Label textLabel;

    private boolean isTyping = false;
    private float typeTimer = 0f;
    private String targetText = "";

    private final float DAVE_X = 145f;
    private final float DAVE_Y = 310f;
    private final float DAVE_SCALE = 0.36f;
    private final String DAVE_PAM_PATH = "768/INITIAL/CRAZYDAVE/CRAZYDAVE/CRAZYDAVE.PAM";

    private final String[] talkAnimations = {
        "anim_smalltalk",
        "anim_mediumtalk",
        "anim_blahblah",
        "anim_crazyblahblah",
    };

    public CrazyDaveOverlay(Main game, List<String> dialogs, GameWorld world) {
        this.game = game;
        this.dialogs = dialogs;
        this.world = world;

        setFillParent(true);
        setVisible(false);

        davePam = new PamPlayer(game.textureBank, Gdx.files.internal(""));
        davePam.loadAsync(DAVE_PAM_PATH, null);

        TextureRegion bubbleReg = game.textureBank.region("IMAGE_STORE_SPEECHBUBBLE2");
        bubbleImage = new Image(bubbleReg);

        bubbleImage.setPosition(DAVE_X + 30f, DAVE_Y + 20f);
        bubbleImage.setVisible(false);

        Label.LabelStyle baseStyle = game.skin.get(Label.LabelStyle.class);

        Label.LabelStyle textStyle = new Label.LabelStyle(baseStyle);
        textStyle.fontColor = Color.BLACK;

        if (textStyle.font != null && textStyle.font.getRegion() != null && textStyle.font.getRegion().getTexture() != null) {
            textStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        textLabel = new Label("", textStyle);
        textLabel.setFontScale(1.25f);
        textLabel.setWrap(true);

        if (bubbleReg != null) {
            textLabel.setWidth(bubbleReg.getRegionWidth() - 65);
        } else {
            textLabel.setWidth(270);
        }

        textLabel.setAlignment(Align.topLeft);
        textLabel.setPosition(bubbleImage.getX() + 32, bubbleImage.getY() + bubbleImage.getHeight() - 30);
        textLabel.setVisible(false);

        addActor(bubbleImage);
        addActor(textLabel);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (finished || !isStarted || isLeaving || currentAnim.equals("anim_enter")) return;

                if (isTyping) {
                    isTyping = false;
                    textLabel.setText(targetText);
                    currentAnim = "anim_idle";
                } else {
                    currentDialogIndex++;
                    if (currentDialogIndex >= dialogs.size()) {
                        startLeaving();
                    } else {
                        startTyping(dialogs.get(currentDialogIndex));
                    }
                }
            }
        });
    }

    public void startPresentation() {
        if (dialogs.isEmpty()) return;
        isStarted = true;
        setVisible(true);
        world.setDialogActive(true);
        stateTime = 0f;
        currentAnim = "anim_enter";
    }

    private void startTyping(String text) {
        targetText = text;
        typeTimer = 0f;
        isTyping = true;
        textLabel.setText("");

        currentAnim = talkAnimations[MathUtils.random(talkAnimations.length - 1)];

        bubbleImage.setVisible(true);
        textLabel.setVisible(true);
    }

    private void startLeaving() {
        isLeaving = true;
        currentAnim = "anim_leave";
        stateTime = 0f;
        bubbleImage.setVisible(false);
        textLabel.setVisible(false);
    }

    private void finishPresentation() {
        finished = true;
        setVisible(false);
        world.setDialogActive(false);

        if (onCompleteAction != null) {
            onCompleteAction.run();
        }
        remove();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (finished || !isStarted) return;
        stateTime += delta;

        if (isLeaving) {
            if (stateTime > 1.2f) {
                finishPresentation();
            }
            return;
        }

        if (currentAnim.equals("anim_enter") && stateTime > 1.2f) {
            startTyping(dialogs.get(currentDialogIndex));
        }

        if (isTyping) {
            typeTimer += delta;
            int charsToShow = (int) (typeTimer * 30);

            if (charsToShow >= targetText.length()) {
                charsToShow = targetText.length();
                isTyping = false;
                currentAnim = "anim_idle";
            }
            textLabel.setText(targetText.substring(0, charsToShow));
        }
    }

    public void startPresentation(List<String> newDialogs, Runnable onComplete) {
        if (newDialogs == null || newDialogs.isEmpty()) {
            if (onComplete != null) onComplete.run();
            return;
        }
        this.dialogs.clear();
        this.dialogs.addAll(newDialogs);
        this.onCompleteAction = onComplete;
        this.currentDialogIndex = 0;
        this.isStarted = true;
        this.finished = false;
        this.isLeaving = false;
        this.setVisible(true);
        this.world.setDialogActive(true);
        this.stateTime = 0f;
        this.currentAnim = "anim_enter";
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (finished || !isStarted) return;

        Matrix4 oldMatrix = batch.getTransformMatrix().cpy();
        Matrix4 newMatrix = new Matrix4(oldMatrix);

        newMatrix.translate(DAVE_X, DAVE_Y, 0);
        newMatrix.scale(DAVE_SCALE, DAVE_SCALE, 1f);
        newMatrix.translate(-DAVE_X, -DAVE_Y, 0);

        batch.setTransformMatrix(newMatrix);

        try {
            davePam.draw(batch, DAVE_PAM_PATH, currentAnim, stateTime, DAVE_X, DAVE_Y, true);
        } catch (Exception e) {
        }

        batch.setTransformMatrix(oldMatrix);

        super.draw(batch, parentAlpha);
    }

    public boolean isStarted() { return isStarted; }
}
