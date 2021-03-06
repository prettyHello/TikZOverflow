package be.ac.ulb.infof307.g09.view.editor;

import be.ac.ulb.infof307.g09.controller.Canvas.ActiveCanvas;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.ShapeDTO;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Shape;
import org.fxmisc.richtext.CodeArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighlightTextColor extends CodeArea {

    private List<Shape> selectedShapes = new ArrayList<>();
    private String wrongLine = null;

    public HighlightTextColor() {
        super();
        keyValidation();
    }

    public void setSelectedShapes(List<Shape> selectedShapes) {
        this.selectedShapes = selectedShapes;
    }

    /**
     * Set the value of the line that it's wrong formatted.
     * @param line the value of the line that is wrong.
     */
    public void setWrongLine(String line) { this.wrongLine = line; }

    /**
     * color an identical word by its position, in a text field
     *
     * @param wordListOption word list for coloring
     * @param color          Style to apply to the word
     */
    public void colorWord(String wordListOption, String color) {
        String[] wordList = getWordList(wordListOption);
        for (String s : wordList) {
            List<Integer> positions = findWordUpgrade(this.getText(), s);
            for (Integer position : positions) {
                this.setStyleClass(position, position + s.length(), color);
            }
        }
    }

    /**
     * select a word list
     *
     * @param wordListOption
     * @return
     */
    private String[] getWordList(String wordListOption) {
        String[] keyWord = {"filldraw", "draw", "path", "node", "tikzstyle", "fill", "documentclass", "usepackage", "begin", "end"}; // Words use by tikz and that will be highlight.
        String[] keyCharacter = {"\\", ",", ";", "[", "]", "(", ")", "{", "}", "$", "#", "%", "*"};
        String[] shapes = {"circle", "rectangle", "cycle", "->"};
        String[] digit = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};
        String[] wordList = null;

        switch (wordListOption) {
            case "keyWord":
                wordList = keyWord;
                break;
            case "keyCharacter":
                wordList = keyCharacter;
                break;
            case "shapes":
                wordList = shapes;
                break;
            case "digit":
                wordList = digit;
                break;
        }
        return wordList;
    }

    /**
     * Return the positions of each given word in a string
     */
    private List<Integer> findWordUpgrade(String textString, String word) {
        List<Integer> indexes = new ArrayList<>();
        String lowerCaseTextString = textString.toLowerCase();
        String lowerCaseWord = word.toLowerCase();
        int wordLength = 0;

        int index = 0;
        while (index != -1) {
            index = lowerCaseTextString.indexOf(lowerCaseWord, index + wordLength);  // Slight improvement
            if (index != -1) {
                indexes.add(index);
            }
            wordLength = word.length();
        }
        return indexes;
    }

    /**
     * Apply a regex on a string
     */
    private static String printMatches(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        matcher.find();
        return matcher.group();
    }

    public void highlightOnSelect() {
        if (!getText().isEmpty()) { // Check if the canvas isn't empty (for example at start) Because if the canvas is empty it will make an error.
            colorWord("keyWord", "keyWord");
            colorWord("keyCharacter", "keyCharacter");
            colorWord("shapes", "shapes");
            colorWord("digit", "digit");
        }

        if (!selectedShapes.isEmpty()) { // If user select a shape.{
            for (int z = 0; z < selectedShapes.size(); z++) { // Loop for every shape selected.
                String myShape = selectedShapes.get(selectedShapes.size() - (z + 1)).toString(); // Selection of the actual selected shape.
                int id = Integer.parseInt(printMatches(myShape, "(id=)[0-9]*").substring(3)); // Take the id of the shape from the var selectedShapes.
                ShapeDTO mySelectedShapeDTO = ActiveCanvas.getActiveCanvas().getShapeById(id);   // Take the shape with the id.
                List<Integer> positions = findWordUpgrade(ActiveCanvas.getActiveCanvas().toTikZ(), mySelectedShapeDTO.print()); // Get the line position in the canvas of the selected shape.
                for (Integer position : positions) {
                    setStyleClass(position, position + mySelectedShapeDTO.print().length(), "highlight"); // Highlight the position of the shape.
                }
            }
        }
    }

    /**
     * action which is triggered each time entered on the keyboard
     */
    private void keyValidation() {
        setOnKeyReleased(key -> {
            final Pattern whiteSpace = Pattern.compile("^\\s+");

            //allows you to re-color the keyword: redetermine the positions each time you enter the keyboard
            if (key.getCode().isDigitKey() ||
                    key.getCode().isFunctionKey() ||
                    key.getCode().isArrowKey() ||
                    key.getCode().isKeypadKey() ||
                    key.getCode().isLetterKey() ||
                    key.getCode().isMediaKey() ||
                    key.getCode().isModifierKey() ||
                    key.getCode().isNavigationKey() ||
                    key.getCode().isWhitespaceKey() ||
                    key.getCode() == KeyCode.BACK_SPACE) {

                setStyleClass(0, getText().length(), "resetColor");
                colorWord("keyWord", "keyWord");
                colorWord("keyCharacter", "keyCharacter");
                colorWord("shapes", "shapes");
                colorWord("digit", "digit");
                highlightOnSelect();
                if (this.wrongLine != null) {
                    highLightWrongLine();
                }
            }

            //allows to add a line number
            if (key.getCode() == KeyCode.ENTER) {
                int caretPosition = getCaretPosition();
                int currentParagraph = getCurrentParagraph();
                if (currentParagraph != 0) {
                    Matcher lineNumber = whiteSpace.matcher(getParagraph(currentParagraph - 1).getSegments().get(0));
                    if (lineNumber.find()) {
                        Platform.runLater(() -> insertText(caretPosition, lineNumber.group()));
                    }
                }
            }
        });
    }

    public void highLightWrongLine() {
        List<String> lines = new ArrayList<>(Arrays.asList(this.getText().split("\\n")));
        int offset = 0;
        for (String line : lines) {
            if (line.equals(this.wrongLine)) {
                List<Integer> positions = findWordUpgrade(line, this.wrongLine);
                for (Integer position : positions) {
                    setStyleClass(position + offset, position + offset + this.wrongLine.length(), "wrong-line");
                }
            }
            offset += line.length() + 1;
        }
    }

}
