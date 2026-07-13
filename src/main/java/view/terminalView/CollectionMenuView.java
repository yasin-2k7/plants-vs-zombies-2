package view.terminalView;

import view.View;

public class CollectionMenuView implements View {
    private static ChapterMenuView instance;
    public ChapterMenuView getInstance(){
        if (instance == null){
            instance = new ChapterMenuView();
            return instance;
        }
        return instance;
    }
    @Override
    public void processCommand(String command) {

    }
}
