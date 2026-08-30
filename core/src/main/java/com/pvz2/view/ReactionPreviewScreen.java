package com.pvz2.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pvz2.Main;
import com.pvz2.network.onlineIZombie.messages.ReactionCategory;

/**
 * TEMPORARY screen, only for visually checking the reaction system with your real assets
 * before OnlineGameScreen/GameScreen is wired up for real matches. No server round trip —
 * picking something in the panel shows the bubble on THIS same screen, immediately.
 * Delete this file (and the button that opens it) once the real screen is ready.
 */
public class ReactionPreviewScreen extends MenuScreen {

    private Container<ReactionPanel> reactionPanelContainer;
    private Table opponentBubbleLayer;

    public ReactionPreviewScreen(Main game) {
        super(game);
    }

    @Override
    protected void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setBackground(new TextureRegionDrawable(game.textureBank.region("IMAGE_MAINMENU_BACKGROUND")));
        mainStack.add(mainTable);

        ImageButton backBtn = MainMenuScreen.createImageButton(
            "IMAGE_UI_MAINMENU_BACK_BTN_NORMAL",
            "IMAGE_UI_MAINMENU_BACK_BTN_PRESSED",
            game.textureBank
        );
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fadeAndSwitchScreen(new MainMenuScreen(game));
            }
        });
        Table topBar = new Table();
        topBar.top().add(backBtn).pad(10);
        topBar.add().expandX();
        mainTable.add(topBar).top().growX().row();
        mainTable.add().expandY().row();

        ReactionPanel reactionPanel = new ReactionPanel(game, skin, this::onPick);
        reactionPanelContainer = new Container<>(reactionPanel);
        reactionPanelContainer.size(440, 420);
        reactionPanelContainer.setVisible(false);

        ImageButton toggleBtn = MainMenuScreen.createImageButton(
            "IMAGE_UI_MAINMENU_EDIT_BTN_PRESSED",
            "IMAGE_UI_MAINMENU_EDIT_BTN_NORMAL",
            game.textureBank
        );
        toggleBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleReactionPanel();
            }
        });

        Table dock = new Table();
        dock.setFillParent(true);
        dock.bottom().right().pad(20);
        dock.add(reactionPanelContainer).padBottom(10).row();
        dock.add(toggleBtn);
        mainStack.add(dock);

        opponentBubbleLayer = new Table();
        opponentBubbleLayer.setFillParent(true);
        opponentBubbleLayer.top().left().pad(20);
        mainStack.add(opponentBubbleLayer);
    }

    private void toggleReactionPanel() {
        if (reactionPanelContainer.isVisible()) {
            reactionPanelContainer.addAction(Actions.sequence(
                Actions.fadeOut(0.25f),
                Actions.visible(false)
            ));
        } else {
            reactionPanelContainer.getColor().a = 0f;
            reactionPanelContainer.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.fadeIn(0.25f)
            ));
        }
    }

    /** Loopback: what would normally go to the server + come back as the opponent's
     *  bubble is shown here directly, so you can see both halves without a match. */
    private void onPick(ReactionCategory category, int index) {
        Actor bubble = ReactionBubbleFactory.build(game, skin, category, index);
        opponentBubbleLayer.clearChildren();
        opponentBubbleLayer.add(bubble);
        bubble.getColor().a = 0f;
        bubble.addAction(Actions.sequence(
            Actions.fadeIn(0.2f),
            Actions.delay(2.5f),
            Actions.fadeOut(0.4f)
        ));
    }
}
