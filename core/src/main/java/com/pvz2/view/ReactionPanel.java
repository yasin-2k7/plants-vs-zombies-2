package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pvz2.Main;
import com.pvz2.network.onlineIZombie.messages.ReactionCategory;
import pvz.skin.BorderedTable;

import java.util.function.BiConsumer;

public class ReactionPanel extends BorderedTable {
    private final Main game;
    private final Skin skin;
    private final BiConsumer<ReactionCategory, Integer> onPick;

    private final Table tabsRow = new Table();
    private final Table content = new Table();

    public ReactionPanel(Main game, Skin skin, BiConsumer<ReactionCategory, Integer> onPick) {
        this.game = game;
        this.skin = skin;
        this.onPick = onPick;
        build();
        showCategory(ReactionCategory.TEXT);
    }

    private void build() {
        top();
        pad(10);

        TextButton textTab = new TextButton("TEXT", skin);
        TextButton emojiTab = new TextButton("EMOJI", skin);
        TextButton stickerTab = new TextButton("STICKER", skin);
        tabsRow.add(textTab).pad(4);
        tabsRow.add(emojiTab).pad(4);
        tabsRow.add(stickerTab).pad(4);

        textTab.addListener(tabListener(ReactionCategory.TEXT));
        emojiTab.addListener(tabListener(ReactionCategory.EMOJI));
        stickerTab.addListener(tabListener(ReactionCategory.STICKER));

        add(tabsRow).row();
        add(content).grow().pad(10);
    }

    private ClickListener tabListener(ReactionCategory category) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showCategory(category);
            }
        };
    }

    private void showCategory(ReactionCategory category) {
        content.clear();
        switch (category) {
            case TEXT -> buildTextContent();
            case EMOJI -> buildEmojiContent();
            case STICKER -> buildStickerContent();
        }
    }

    private void buildTextContent() {
        for (int i = 0; i < ReactionAssets.TEXTS.length; i++) {
            int index = i;
            TextButton btn = new TextButton(ReactionAssets.TEXTS[i], skin, "medium");
            btn.getLabel().setWrap(true);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onPick.accept(ReactionCategory.TEXT, index);
                }
            });
            content.add(btn).width(240).padBottom(8).row();
        }
    }

    private void buildEmojiContent() {
        for (int i = 0; i < ReactionAssets.EMOJI_REGIONS.length; i++) {
            int index = i;
            TextureRegion region = game.textureBank.region(ReactionAssets.EMOJI_REGIONS[i]);
            ImageButton btn = new ImageButton(new TextureRegionDrawable(region));
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onPick.accept(ReactionCategory.EMOJI, index);
                }
            });
            content.add(btn).size(64).pad(8);
        }
    }

    private void buildStickerContent() {
        for (int i = 0; i < ReactionAssets.STICKER_PATHS.length; i++) {
            int index = i;
            PamActor sticker = new PamActor(game.pamPlayer, ReactionAssets.STICKER_PATHS[i],
                ReactionAssets.STICKER_ANIMATIONS[i], 1, null);
            Stack stack = new Stack();
            stack.setSize(80, 80);
            stack.add(sticker);
            stack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onPick.accept(ReactionCategory.STICKER, index);
                }
            });
            content.add(stack).size(80).pad(8);
        }
    }
}
