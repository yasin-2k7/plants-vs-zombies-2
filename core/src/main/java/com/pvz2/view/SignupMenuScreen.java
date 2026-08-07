package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.pvz2.Main;
import com.pvz2.controller.SignupMenuController;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.textures.TextureBank;
import pvz.skin.BorderedTable;

import java.util.List;
import java.util.function.Supplier;

public class SignupMenuScreen extends MenuScreen {

    TextureRegion textureRegion;
    private TextureBank textureBank;

    private SignupMenuController controller;
    private NinePatchDrawable errorBorderDrawable;

    private static class ValidatedField {
        Actor field;
        Label errorLabel;
        Object style;
        Object defaultBackground;
    }

    public SignupMenuScreen(Main game) {
        super(game);

        FileHandle assetsFolder = Gdx.files.internal("");
        textureBank = new TextureBank("786", assetsFolder);
        textureRegion = textureBank.region("IMAGE_UI_CALENDAR_CALENDAR_CARD_7DAY_FOODFIGHT");
    }

    @Override
    protected void buildUI() {
        controller = new SignupMenuController(this);
        errorBorderDrawable = createBorderDrawable(Color.RED, 3);

        Image backgroundImage = new Image(textureRegion);
        mainStack.add(backgroundImage);

        BorderedTable formTable = new BorderedTable();
        formTable.pad(25);

        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("username");
        ValidatedField usernameVF = wrapTextField(usernameField);
        attachFocusValidation(usernameVF, () -> controller.getUsernameErrors(usernameField.getText()));

        TextField nicknameField = new TextField("", skin);
        nicknameField.setMessageText("nickname");
        ValidatedField nicknameVF = wrapTextField(nicknameField);
        attachFocusValidation(nicknameVF, () -> controller.getNicknameErrors(nicknameField.getText()));

        TextField emailField = new TextField("", skin);
        emailField.setMessageText("email");
        ValidatedField emailVF = wrapTextField(emailField);
        attachFocusValidation(emailVF, () -> controller.getEmailErrors(emailField.getText()));

        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Password");
        ValidatedField passwordVF = wrapTextField(passwordField);

        TextField passwordConfirmField = new TextField("", skin);
        passwordConfirmField.setPasswordMode(true);
        passwordConfirmField.setPasswordCharacter('*');
        passwordConfirmField.setMessageText("Confirm password");
        ValidatedField passwordConfirmVF = wrapTextField(passwordConfirmField);

        attachFocusValidation(passwordVF,
            () -> controller.validatePasswordStrength(passwordField.getText()));
        attachFocusValidation(passwordConfirmVF,
            () -> controller.getPasswordErrors(passwordField.getText(), passwordConfirmField.getText()));

        SelectBox<String> genderBox = new SelectBox<>(skin);
        genderBox.setItems("male", "female");
        ValidatedField genderVF = wrapSelectBox(genderBox);
        attachChangeValidation(genderVF, genderBox,
            () -> controller.getGenderErrors(genderBox.getSelected()));

        SelectBox<String> questionBox = new SelectBox<>(skin);
        Array<String> questionItems = new Array<>();
        for (String q : controller.getQuestions()) questionItems.add(q);
        questionBox.setItems(questionItems);
        ValidatedField questionVF = wrapSelectBox(questionBox);

        TextField answerField = new TextField("", skin);
        answerField.setMessageText("answer");
        ValidatedField answerVF = wrapTextField(answerField);

        TextField answerConfirmField = new TextField("", skin);
        answerConfirmField.setMessageText("confirm answer");
        ValidatedField answerConfirmVF = wrapTextField(answerConfirmField);

        Supplier<List<String>> answerValidator = () -> controller.getAnswerErrors(
            questionBox.getSelectedIndex() + 1,
            answerField.getText());

        Supplier<List<String>> answerConfirmValidator = () -> controller.getAnswerConfirmErrors(
            questionBox.getSelectedIndex() + 1,
            answerField.getText(),
            answerConfirmField.getText());

        attachFocusValidation(answerVF, answerValidator);
        attachFocusValidation(answerConfirmVF, answerConfirmValidator);

        TextButton signupBtn = new TextButton("Sign Up", skin);
        signupBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean usernameOk = showFieldErrors(usernameVF, controller.getUsernameErrors(usernameField.getText()));
                boolean nicknameOk = showFieldErrors(nicknameVF, controller.getNicknameErrors(nicknameField.getText()));
                boolean emailOk = showFieldErrors(emailVF, controller.getEmailErrors(emailField.getText()));
                boolean passwordOk = showFieldErrors(passwordVF, controller.validatePasswordStrength(passwordField.getText()));
                boolean passwordConfirmOk = showFieldErrors(passwordConfirmVF,
                    controller.getPasswordErrors(passwordField.getText(), passwordConfirmField.getText()));
                boolean genderOk = showFieldErrors(genderVF, controller.getGenderErrors(genderBox.getSelected()));
                boolean answerOk = showFieldErrors(answerVF, answerValidator.get());
                boolean answerConfirmOk = showFieldErrors(answerConfirmVF, answerConfirmValidator.get());

                boolean allOk = usernameOk && nicknameOk && emailOk && passwordOk
                    && passwordConfirmOk && genderOk && answerOk && answerConfirmOk;

                if (!allOk) return;
                controller.createUser(usernameField.getText(), passwordField.getText(),
                    nicknameField.getText(), emailField.getText(), genderBox.getSelected(),
                    questionBox.getSelected(), answerField.getText());
                controller.changeMenu();
            }
        });

        TextButton loginBtn = new TextButton("Login", skin);
        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.changeMenu();
            }
        });

        addRow(formTable, usernameVF);
        addRow(formTable, nicknameVF);
        addRow(formTable, emailVF);
        addRow(formTable, passwordVF);
        addRow(formTable, passwordConfirmVF);
        addRow(formTable, genderVF);

        BorderedTable securityTable = new BorderedTable();
        securityTable.pad(25);

        addRow(securityTable, questionVF);
        addRow(securityTable, answerVF);
        addRow(securityTable, answerConfirmVF);
        securityTable.add(signupBtn).width(150).padTop(10).row();
        securityTable.add(loginBtn).width(150).padTop(10);

        Table wrapper = new Table();
        wrapper.center();
        wrapper.add(formTable).top().padRight(20);
        wrapper.add(securityTable).top();

        mainStack.add(wrapper);
    }

    private ValidatedField wrapTextField(TextField field) {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle(field.getStyle());
        field.setStyle(style);

        ValidatedField vf = new ValidatedField();
        vf.field = field;
        vf.style = style;
        vf.defaultBackground = style.background;
        vf.errorLabel = createErrorLabel();
        return vf;
    }

    private ValidatedField wrapSelectBox(SelectBox<String> box) {
        SelectBox.SelectBoxStyle style = new SelectBox.SelectBoxStyle(box.getStyle());
        box.setStyle(style);

        ValidatedField vf = new ValidatedField();
        vf.field = box;
        vf.style = style;
        vf.defaultBackground = style.background;
        vf.errorLabel = createErrorLabel();
        return vf;
    }

    private Label createErrorLabel() {
        Label label = new Label("", skin);
        label.setColor(Color.RED);
        label.setFontScale(0.8f);
        label.setWrap(true);
        label.setVisible(false);
        return label;
    }

    private void attachFocusValidation(ValidatedField vf, Supplier<List<String>> validator) {
        vf.field.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (focused) return;
                showFieldErrors(vf, validator.get());
            }
        });
    }

    private void attachChangeValidation(ValidatedField vf, SelectBox<String> box, Supplier<List<String>> validator) {
        box.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showFieldErrors(vf, validator.get());
            }
        });
    }

    private boolean showFieldErrors(ValidatedField vf, List<String> errors) {
        boolean valid = errors == null || errors.isEmpty();

        if (vf.style instanceof TextField.TextFieldStyle) {
            TextField.TextFieldStyle s = (TextField.TextFieldStyle) vf.style;
            s.background = valid ? (com.badlogic.gdx.scenes.scene2d.utils.Drawable) vf.defaultBackground : errorBorderDrawable;
        } else if (vf.style instanceof SelectBox.SelectBoxStyle) {
            SelectBox.SelectBoxStyle s = (SelectBox.SelectBoxStyle) vf.style;
            s.background = valid ? (com.badlogic.gdx.scenes.scene2d.utils.Drawable) vf.defaultBackground : errorBorderDrawable;
        }

        if (valid) {
            vf.errorLabel.setVisible(false);
            vf.errorLabel.setText("");
        } else {
            vf.errorLabel.setText(String.join("\n", errors));
            vf.errorLabel.setVisible(true);
            shake(vf.field);
        }

        return valid;
    }

    private void shake(Actor actor) {
        actor.clearActions();
        final float originalX = actor.getX();
        actor.addAction(Actions.sequence(
            Actions.moveBy(8, 0, 0.04f),
            Actions.moveBy(-16, 0, 0.04f),
            Actions.moveBy(16, 0, 0.04f),
            Actions.moveBy(-16, 0, 0.04f),
            Actions.moveBy(8, 0, 0.04f),
            Actions.run(() -> actor.setX(originalX))
        ));
    }

    private void addRow(Table table, ValidatedField vf) {
        table.add(vf.field).width(300).padBottom(2).row();
        table.add(vf.errorLabel).width(300).padBottom(10).left().row();
    }

    private NinePatchDrawable createBorderDrawable(Color borderColor, int thickness) {
        int size = thickness * 3;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        pixmap.setColor(borderColor);
        for (int i = 0; i < thickness; i++) {
            pixmap.drawRectangle(i, i, size - i * 2, size - i * 2);
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        NinePatch patch = new NinePatch(texture, thickness, thickness, thickness, thickness);
        return new NinePatchDrawable(patch);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
