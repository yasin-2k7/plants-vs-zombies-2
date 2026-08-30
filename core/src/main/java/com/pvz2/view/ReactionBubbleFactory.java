package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pvz2.Main;
import com.pvz2.network.onlineIZombie.messages.ReactionCategory;
import pvz.skin.BorderedTable;

public final class ReactionBubbleFactory {

    public static Actor build(Main game, Skin skin, ReactionCategory category, int index) {
        BorderedTable bubble = new BorderedTable();
        bubble.pad(10);
        switch (category) {
            case TEXT -> {
                Label label = new Label(ReactionAssets.TEXTS[index], skin, "medium");
                label.setWrap(true);
                bubble.add(label).width(220);
            }
            case EMOJI -> {
                TextureRegion region = game.textureBank.region(ReactionAssets.EMOJI_REGIONS[index]);
                bubble.add(new Image(region)).size(64);
            }
            case STICKER -> {
                PamActor sticker = new PamActor(game.pamPlayer, ReactionAssets.STICKER_PATHS[index],
                    ReactionAssets.STICKER_ANIMATIONS[index], 1, null);
                bubble.add(sticker).size(96);
            }
        }
        return bubble;
    }

    private ReactionBubbleFactory() {}
}
