import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Base class for AST nodes
abstract class ASTNode {
    public abstract void print(); // Method to print the AST for visualization
}

// Operand Node (for simple values like a, b, etc.)
class OperandNode extends ASTNode {
    String value;

    public OperandNode(String value) {
        this.value = value;
    }

    @Override
    public void print() {
        System.out.println(value);
    }
}

// Arithmetic Operations (Binary)
class AddNode extends ASTNode {
    ASTNode left, right;

    public AddNode(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void print() {
        System.out.println("+");
        left.print();
        right.print();
    }
}

// MultiplyNode class (for multiplication operation)
class MultiplyNode extends ASTNode {
    ASTNode left, right;

    public MultiplyNode(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void print() {
        System.out.println("*");
        left.print();
        right.print();
    }
}

// Parser class to generate AST from input expression (supporting + and *)
class ASTParser {
    private StringTokenizer tokenizer;
    private String currentToken;

    public ASTNode parse(String expression) {
        tokenizer = new StringTokenizer(expression, "+*", true);
        nextToken();
        return parseExpression();
    }

    private void nextToken() {
        currentToken = tokenizer.hasMoreTokens() ? tokenizer.nextToken().trim() : null;
    }

    private ASTNode parseExpression() {
        ASTNode node = parseTerm();
        while (currentToken != null && currentToken.equals("+")) {
            nextToken();
            node = new AddNode(node, parseTerm());
        }
        return node;
    }

    private ASTNode parseTerm() {
        ASTNode node = parseFactor();
        while (currentToken != null && currentToken.equals("*")) {
            nextToken();
            node = new MultiplyNode(node, parseFactor());
        }
        return node;
    }

    private ASTNode parseFactor() {
        ASTNode node = new OperandNode(currentToken);
        nextToken();
        return node;
    }
}

public class CompilerUI {
    private JFrame frame;
    private JTextArea editorTextArea;
    private JTextArea outputTextArea;
    private JButton compileButton, tokenizeButton, implementButton, pdfButton, generateTACButton;
    private JLabel outputLabel;

    // Predefined valid functions for the compiler with return types
    private static final Map<String, String> predefinedFunctions = new HashMap<>();

    static {
        predefinedFunctions.put("add", "int");
        predefinedFunctions.put("subtract", "int");
        predefinedFunctions.put("multiply", "int");
        predefinedFunctions.put("divide", "int");
        predefinedFunctions.put("modulus", "int");
        predefinedFunctions.put("power", "double");
        predefinedFunctions.put("squareroot", "double");
        predefinedFunctions.put("cuberoot", "double");
        predefinedFunctions.put("nthroot", "double");
        predefinedFunctions.put("iseven", "boolean");
        predefinedFunctions.put("isodd", "boolean");
        predefinedFunctions.put("halfvalue", "double");
        predefinedFunctions.put("doublevalue", "double");
        predefinedFunctions.put("increment", "int");
        predefinedFunctions.put("decrement", "int");
        predefinedFunctions.put("findmax", "int");
        predefinedFunctions.put("findmin", "int");
        predefinedFunctions.put("isprime", "boolean");
        predefinedFunctions.put("sin", "double");
        predefinedFunctions.put("cos", "double");
        predefinedFunctions.put("tan", "double");
        predefinedFunctions.put("cot", "double");
        predefinedFunctions.put("sec", "double");
        predefinedFunctions.put("cosec", "double");
        predefinedFunctions.put("gcd", "int");
        predefinedFunctions.put("lcm", "int");
        predefinedFunctions.put("absolutevalue", "int");
        predefinedFunctions.put("ceil", "double");
        predefinedFunctions.put("floor", "double");
        predefinedFunctions.put("round", "int");
        predefinedFunctions.put("absolutedifference", "int");
        predefinedFunctions.put("ispositive", "boolean");
        predefinedFunctions.put("isperfectsquare", "boolean");
        predefinedFunctions.put("cubeofdiff", "int");
        predefinedFunctions.put("averageof3", "double");
        predefinedFunctions.put("ismultiple", "boolean");
        predefinedFunctions.put("sumofdigits", "int");
        predefinedFunctions.put("sumofsquares", "int");
        predefinedFunctions.put("reciprocal", "double");
        predefinedFunctions.put("mean", "double");
        predefinedFunctions.put("reversenumber", "int");
        predefinedFunctions.put("degreestoradians", "double");
        predefinedFunctions.put("radianstodegrees", "double");
        predefinedFunctions.put("percentage", "double");
        predefinedFunctions.put("areaofsquare", "double");
        predefinedFunctions.put("areaofrectangle", "double");
        predefinedFunctions.put("areaofcircle", "double");
        predefinedFunctions.put("maxofthree", "int");
        predefinedFunctions.put("minofthree", "int");
        predefinedFunctions.put("ispalindrome", "boolean");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CompilerUI window = new CompilerUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CompilerUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Compiler UI");
        frame.setBounds(100, 100, 700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.LIGHT_GRAY);

        JButton pdfButton = new JButton("User Manual");
        pdfButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pdfButton.setFont(new Font("Arial", Font.BOLD, 14));
        pdfButton.setBackground(new Color(0, 123, 255));
        pdfButton.setForeground(Color.WHITE);
        pdfButton.setMaximumSize(new Dimension(150, 40));
        pdfButton.addActionListener(event -> showPDFTutorial());
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(pdfButton);
        menuPanel.add(Box.createVerticalGlue());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        editorTextArea = new JTextArea();
        editorTextArea.setFont(new Font("Courier New", Font.PLAIN, 16));
        editorTextArea.setBackground(Color.WHITE);
        JScrollPane scrollEditor = new JScrollPane(editorTextArea);
        scrollEditor.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 123, 255), 2), "Editor"));

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputLabel = new JLabel("");
        outputLabel.setFont(new Font("Arial", Font.BOLD, 16));
        outputLabel.setForeground(Color.BLACK);
        outputPanel.add(outputLabel, BorderLayout.NORTH);

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setFont(new Font("Courier New", Font.PLAIN, 16));
        outputTextArea.setBackground(Color.WHITE);
        JScrollPane scrollOutput = new JScrollPane(outputTextArea);
        scrollOutput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 123, 255), 2), "Output"));
        outputPanel.add(scrollOutput, BorderLayout.CENTER);

        JSplitPane editorOutputSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollEditor, outputPanel);
        editorOutputSplitPane.setDividerLocation(350);
        editorOutputSplitPane.setResizeWeight(0.5);
        editorOutputSplitPane.setOneTouchExpandable(true);
        mainPanel.add(editorOutputSplitPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        Color unifiedColor = new Color(0, 123, 255); // Bootstrap Blue

        compileButton = createButton("Compile", unifiedColor, e -> compileCode());
        tokenizeButton = createButton("Tokenize", unifiedColor, e -> tokenizeFunction());
        implementButton = createButton("Implement", unifiedColor, e -> implementFunction());
        generateTACButton = createButton("TAC", unifiedColor, e -> generateTAC());
        JButton symbolTableButton = createButton("Symbol Table", unifiedColor, e -> showSymbolTable());
        JButton astButton = createButton("AST", unifiedColor, e -> displayAST());

        buttonPanel.add(compileButton);
        buttonPanel.add(tokenizeButton);
        buttonPanel.add(implementButton);
        buttonPanel.add(generateTACButton);
        buttonPanel.add(symbolTableButton);
        buttonPanel.add(astButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, mainPanel);
        splitPane.setDividerLocation(150);
        splitPane.setOneTouchExpandable(true);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    private JButton createButton(String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(140, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    private void showSymbolTable() {
        JFrame tableFrame = new JFrame("Symbol Table");
        tableFrame.setSize(800, 400);

        String[] columnNames = { "Name", "Parameters", "Datatypes", "No. of Parameters", "Scope", "Size",
                "Return Type" };
        Object[][] data = {
                { "Add", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Subtract", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Multiply", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Divide", "a, b", "double, double", 2, "Global", 16, "double" },
                { "Modulus", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Power", "base, exponent", "double, double", 2, "Global", 16, "double" },
                { "Squareroot", "a", "double", 1, "Global", 8, "double" },
                { "Cuberoot", "a", "double", 1, "Global", 8, "double" },
                { "Nth root", "a, n", "double, double", 2, "Global", 16, "double" },
                { "Is even", "a", "int", 1, "Global", 4, "bool" },
                { "Is odd", "a", "int", 1, "Global", 4, "bool" },
                { "Half value", "a", "double", 1, "Global", 8, "double" },
                { "Double value", "a", "double", 1, "Global", 8, "double" },
                { "Increment", "a", "int", 1, "Global", 4, "int" },
                { "Decrement", "a", "int", 1, "Global", 4, "int" },
                { "Find max", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Find min", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Is prime", "a", "int", 1, "Global", 4, "bool" },
                { "Sin", "a", "double", 1, "Global", 8, "double" },
                { "Cos", "a", "double", 1, "Global", 8, "double" },
                { "Tan", "a", "double", 1, "Global", 8, "double" },
                { "Cot", "a", "double", 1, "Global", 8, "double" },
                { "Sec", "a", "double", 1, "Global", 8, "double" },
                { "Cosec", "a", "double", 1, "Global", 8, "double" },
                { "Gcd", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Lcm", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Absolute value", "a", "int", 1, "Global", 4, "int" },
                { "Ceil", "a", "double", 1, "Global", 8, "double" },
                { "Floor", "a", "double", 1, "Global", 8, "double" },
                { "Round", "a", "double", 1, "Global", 8, "int" },
                { "Absolute difference", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Is positive", "a", "int", 1, "Global", 4, "bool" },
                { "Is perfect square", "a", "int", 1, "Global", 4, "bool" },
                { "Cube of diff", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Average of 3", "a, b, c", "int, int, int", 3, "Global", 12, "double" },
                { "Is multiple", "a, b", "int, int", 2, "Global", 8, "bool" },
                { "Sum of digits", "a", "int", 1, "Global", 4, "int" },
                { "Sum of squares", "a, b", "int, int", 2, "Global", 8, "int" },
                { "Reciprocal", "a", "double", 1, "Global", 8, "double" },
                { "Mean", "arr[], size", "int[], int", 2, "Global", 8 + 4, "double" },
                { "Reverse number", "a", "int", 1, "Global", 4, "int" },
                { "Degrees to radians", "deg", "double", 1, "Global", 8, "double" },
                { "Radians to degree", "rad", "double", 1, "Global", 8, "double" },
                { "Percentage", "a, b", "double, double", 2, "Global", 16, "double" },
                { "Area of square", "side", "double", 1, "Global", 8, "double" },
                { "Area of Rectangle", "l, b", "double, double", 2, "Global", 16, "double" },
                { "Area of circle", "radius", "double", 1, "Global", 8, "double" },
                { "Max of three", "a, b, c", "int, int, int", 3, "Global", 12, "int" },
                { "Min of three", "a, b, c", "int, int, int", 3, "Global", 12, "int" },
                { "Is palindrome", "a", "int", 1, "Global", 4, "bool" }
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        tableFrame.add(scrollPane);
        tableFrame.setVisible(true);
    }

    // Compile and validate code
    private void compileCode() {
        String functionDeclaration = editorTextArea.getText().trim();
        if (functionDeclaration.isEmpty()) {
            outputTextArea.setText("Error: Empty input.");
            return;
        }

        String validationResult = validateFunctionDeclaration(functionDeclaration);
        outputTextArea.setText(validationResult);
    }

    private void displayAST() {
        String code = editorTextArea.getText().trim();

        switch (code) {
            case "a + b":
                outputTextArea.setText(buildTree("  add", "a", "b"));
                break;
            case "a - b":
                outputTextArea.setText(buildTree("  subtract", "a", "b"));
                break;
            case "a * b":
                outputTextArea.setText(buildTree("  multiply", "a", "b"));
                break;
            case "a / b":
                outputTextArea.setText(buildTree("  divide", "a", "b"));
                break;
            case "a % b":
                outputTextArea.setText(buildTree("Modulus", "a", "b"));
                break;
            case "a ^ b":
                outputTextArea.setText(buildTree("Power", "a", "b"));
                break;
            case "sqrt(a)":
                outputTextArea.setText(buildTree("Squareroot", "a", ""));
                break;
            case "cbrt(a)":
                outputTextArea.setText(buildTree("Cuberoot", "a", ""));
                break;
            case "nthRoot(a, b)":
                outputTextArea.setText(buildTree("Nth root", "a", "b"));
                break;
            case "isEven(a)":
                outputTextArea.setText(buildTree("Is even", "a", ""));
                break;
            case "isOdd(a)":
                outputTextArea.setText(buildTree("Is odd", "a", ""));
                break;
            case "half(a)":
                outputTextArea.setText(buildTree("Half value", "a", ""));
                break;
            case "double(a)":
                outputTextArea.setText(buildTree("Double value", "a", ""));
                break;
            case "increment(a)":
                outputTextArea.setText(buildTree("Increment", "a", ""));
                break;
            case "decrement(a)":
                outputTextArea.setText(buildTree("Decrement", "a", ""));
                break;
            case "max(a, b)":
                outputTextArea.setText(buildTree("Find max", "a", "b"));
                break;
            case "min(a, b)":
                outputTextArea.setText(buildTree("Find min", "a", "b"));
                break;
            case "isPrime(a)":
                outputTextArea.setText(buildTree("Is prime", "a", ""));
                break;
            case "sin(a)":
                outputTextArea.setText(buildTree("Sin", "a", ""));
                break;
            case "cos(a)":
                outputTextArea.setText(buildTree("Cos", "a", ""));
                break;
            case "tan(a)":
                outputTextArea.setText(buildTree("Tan", "a", ""));
                break;
            case "cot(a)":
                outputTextArea.setText(buildTree("Cot", "a", ""));
                break;
            case "sec(a)":
                outputTextArea.setText(buildTree("Sec", "a", ""));
                break;
            case "cosec(a)":
                outputTextArea.setText(buildTree("Cosec", "a", ""));
                break;
            case "gcd(a, b)":
                outputTextArea.setText(buildTree("Gcd", "a", "b"));
                break;
            case "lcm(a, b)":
                outputTextArea.setText(buildTree("Lcm", "a", "b"));
                break;
            case "abs(a)":
                outputTextArea.setText(buildTree("Absolute value", "a", ""));
                break;
            case "ceil(a)":
                outputTextArea.setText(buildTree("Ceil", "a", ""));
                break;
            case "floor(a)":
                outputTextArea.setText(buildTree("Floor", "a", ""));
                break;
            case "round(a)":
                outputTextArea.setText(buildTree("Round", "a", ""));
                break;
            case "absDiff(a, b)":
                outputTextArea.setText(buildTree("Absolute difference", "a", "b"));
                break;
            case "isPositive(a)":
                outputTextArea.setText(buildTree("Is positive", "a", ""));
                break;
            case "isPerfectSquare(a)":
                outputTextArea.setText(buildTree("Is perfect square", "a", ""));
                break;
            case "cubeDiff(a, b)":
                outputTextArea.setText(buildTree("Cube of diff", "a", "b"));
                break;
            case "avg(a, b, c)":
                outputTextArea.setText(buildTree("Average of 3", "a", "b"));
                break;
            case "isMultiple(a, b)":
                outputTextArea.setText(buildTree("Is multiple", "a", "b"));
                break;
            case "sumDigits(a)":
                outputTextArea.setText(buildTree("Sum of digits", "a", ""));
                break;
            case "sumSquares(a)":
                outputTextArea.setText(buildTree("Sum of squares", "a", ""));
                break;
            case "reciprocal(a)":
                outputTextArea.setText(buildTree("Reciprocal", "a", ""));
                break;
            case "mean(a, b, c)":
                outputTextArea.setText(buildTree("  Mean", "a", "b"));
                break;
            case "reverseNumber(a)":
                outputTextArea.setText(buildTree("Reverse number", "a", ""));
                break;
            case "degToRad(a)":
                outputTextArea.setText(buildTree("Degrees to radians", "a", ""));
                break;
            case "radToDeg(a)":
                outputTextArea.setText(buildTree("Radians to degree", "a", ""));
                break;
            case "percentage(a, b)":
                outputTextArea.setText(buildTree("Percentage", "a", "b"));
                break;
            case "areaSquare(a)":
                outputTextArea.setText(buildTree("Area of square", "a", ""));
                break;
            case "areaRectangle(a, b)":
                outputTextArea.setText(buildTree("Area of rectangle", "a", "b"));
                break;
            case "areaCircle(a)":
                outputTextArea.setText(buildTree("Area of circle", "a", ""));
                break;
            case "maxOfThree(a, b, c)":
                outputTextArea.setText(buildTree("Max of three", "a", "b"));
                break;
            case "minOfThree(a, b, c)":
                outputTextArea.setText(buildTree("Min of three", "a", "b"));
                break;
            case "isPalindrome(a)":
                outputTextArea.setText(buildTree("Is palindrome", "a", ""));
                break;
            default:
                outputTextArea.setText("Invalid expression format.");
        }
    }

    private String buildTree(String root, String left, String right) {
        if (right.isEmpty()) {
            return root + "\n" + "  " + left;
        } else {
            return root + "\n" + "  / \\ \n" + " " + left + "   " + right;
        }
    }

    private void printAST(ASTNode node, int level) {
        String indent = "  ".repeat(level);
        if (node instanceof OperandNode) {
            outputTextArea.append(indent + ((OperandNode) node).value + "\n");
        } else if (node instanceof AddNode) {
            outputTextArea.append(indent + "Add\n");
            printAST(((AddNode) node).left, level + 1);
            printAST(((AddNode) node).right, level + 1);
        } else if (node instanceof MultiplyNode) {
            outputTextArea.append(indent + "Multiply\n");
            printAST(((MultiplyNode) node).left, level + 1);
            printAST(((MultiplyNode) node).right, level + 1);
        }
    }

    // AST Node Classes
    abstract class ASTNode {
    }

    class AddNode extends ASTNode {
        ASTNode left, right;

        AddNode(ASTNode left, ASTNode right) {
            this.left = left;
            this.right = right;
        }
    }

    class MultiplyNode extends ASTNode {
        ASTNode left, right;

        MultiplyNode(ASTNode left, ASTNode right) {
            this.left = left;
            this.right = right;
        }
    }

    class OperandNode extends ASTNode {
        String value;

        OperandNode(String value) {
            this.value = value;
        }
    }

    // Validate function declaration
    private String validateFunctionDeclaration(String declaration) {
        if (!declaration.endsWith(";")) {
            return "Error: Missing semicolon at the end.";
        }

        String[] tokens = declaration.split("\\(");
        if (tokens.length < 2) {
            return "Error: Invalid function declaration.";
        }

        String header = tokens[0].trim();
        String[] headerParts = header.split("\\s+");
        if (headerParts.length != 2) {
            return "Error: Invalid header format.";
        }

        String functionName = headerParts[1].toLowerCase();
        if (!predefinedFunctions.containsKey(functionName)) {
            return "Error: Undefined Function '" + headerParts[1] + "'";
        }

        return "Function '" + headerParts[1] + "' compiled successfully.";
    }

    // Tokenize and extract function details with labeling
    private void tokenizeFunction() {
        String functionDeclaration = editorTextArea.getText().trim();
        if (functionDeclaration.isEmpty()) {
            outputTextArea.setText("Error: No function to tokenize.");
            return;
        }

        StringBuilder tokenOutput = new StringBuilder("Function Declaration: ").append(functionDeclaration)
                .append("\n");

        // Using StringTokenizer to split and preserve delimiters
        StringTokenizer tokenizer = new StringTokenizer(functionDeclaration, "(),; \t\n\r", true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) {
                continue;
            }

            if (token.equals("int") || token.equals("double") || token.equals("boolean")) {
                tokenOutput.append("Token: ").append(token).append(" -> KT\n");
            } else if (predefinedFunctions.containsKey(token.toLowerCase())) {
                tokenOutput.append("Token: ").append(token).append(" -> IT\n");
            } else if (token.matches("\\w+")) {
                tokenOutput.append("Token: ").append(token).append(" -> IT\n");
            } else if (token.equals("(") || token.equals(")")) {
                tokenOutput.append("Token: ").append(token).append(" -> DT\n");
            } else if (token.equals(",")) {
                tokenOutput.append("Token: ").append(token).append(" -> SCT\n");
            } else if (token.equals(";")) {
                tokenOutput.append("Token: ").append(token).append(" -> DT\n");
            }
        }

        outputTextArea.setText(tokenOutput.toString());
    }

    // Open User Manual
    private void showPDFTutorial() {
        try {
            File pdfFile = new File("C:\\Users\\DELL\\OneDrive\\User Manual CompilerDesign.pdf\\");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                outputTextArea.setText("Error: PDF file not found.");
            }
        } catch (IOException e) {
            outputTextArea.setText("Error: Unable to open PDF.");
        }
    }

    // Implement functionality (to be extended)
    // Implement functionality based on function and arguments
    private void implementFunction() {
        String functionDeclaration = editorTextArea.getText().trim();
        if (functionDeclaration.isEmpty()) {
            outputTextArea.setText("Error: No function to implement.");
            return;
        }

        // Remove semicolon at the end if present
        if (functionDeclaration.endsWith(";")) {
            functionDeclaration = functionDeclaration.substring(0, functionDeclaration.length() - 1);
        }

        // Extract function name and parameters
        int openParenIndex = functionDeclaration.indexOf("(");
        int closeParenIndex = functionDeclaration.indexOf(")");

        if (openParenIndex == -1 || closeParenIndex == -1 || closeParenIndex < openParenIndex) {
            outputTextArea.setText("Error: Invalid function format. Correct format: functionName(param1, param2)");
            return;
        }

        // Extract function header and name
        String header = functionDeclaration.substring(0, openParenIndex).trim();
        String[] headerParts = header.split("\\s+");

        if (headerParts.length < 2) {
            outputTextArea.setText("Error: Invalid function format. No function name found.");
            return;
        }

        String functionName = headerParts[1].trim();

        // Extract parameters
        String paramsString = functionDeclaration.substring(openParenIndex + 1, closeParenIndex).trim();
        String[] params = paramsString.isEmpty() ? new String[0] : paramsString.split(",");

        // Check if the function exists in predefined functions
        if (!predefinedFunctions.containsKey(functionName.toLowerCase())) {
            outputTextArea.setText("Error: Function '" + functionName + "' not recognized.");
            return;
        }

        try {
            String result = executeFunction(functionName.toLowerCase(), params);
            outputTextArea.setText("Result: " + result);
        } catch (NumberFormatException e) {
            outputTextArea.setText("Error: Invalid number format. Please enter valid numeric values.");
        } catch (ArithmeticException e) {
            outputTextArea.setText("Error: " + e.getMessage());
        } catch (Exception e) {
            outputTextArea.setText("Error: " + e.getMessage());
        }
    }

    // Execute the function based on the function name and parameters
    private String executeFunction(String functionName, String[] params) throws Exception {
        if (params.length == 0) {
            throw new IllegalArgumentException("Error: Missing parameters.");
        }

        switch (functionName) {
            case "add":
                return String.valueOf(Integer.parseInt(params[0]) + Integer.parseInt(params[1]));
            case "subtract":
                return String.valueOf(Integer.parseInt(params[0]) - Integer.parseInt(params[1]));
            case "multiply":
                return String.valueOf(Integer.parseInt(params[0]) * Integer.parseInt(params[1]));
            case "divide":
                if (Double.parseDouble(params[1]) == 0)
                    throw new ArithmeticException("Error: Division by zero.");
                return String.valueOf(Double.parseDouble(params[0]) / Double.parseDouble(params[1]));
            case "modulus":
                if (Integer.parseInt(params[1]) == 0)
                    throw new ArithmeticException("Error: Modulo by zero.");
                return String.valueOf(Integer.parseInt(params[0]) % Integer.parseInt(params[1]));
            case "power":
                return String.valueOf(Math.pow(Double.parseDouble(params[0]), Double.parseDouble(params[1])));
            case "squareroot":
                return String.valueOf(Math.sqrt(Double.parseDouble(params[0])));
            case "cuberoot":
                return String.valueOf(Math.cbrt(Double.parseDouble(params[0])));
            case "nthroot":
                return String.valueOf(Math.pow(Double.parseDouble(params[0]), 1.0 / Double.parseDouble(params[1])));
            case "iseven":
                return String.valueOf(Integer.parseInt(params[0]) % 2 == 0);
            case "isodd":
                return String.valueOf(Integer.parseInt(params[0]) % 2 != 0);
            case "halfvalue":
                return String.valueOf(Double.parseDouble(params[0]) / 2);
            case "doublevalue":
                return String.valueOf(Double.parseDouble(params[0]) * 2);
            case "increment":
                return String.valueOf(Integer.parseInt(params[0]) + 1);
            case "decrement":
                return String.valueOf(Integer.parseInt(params[0]) - 1);
            case "findmax":
                return String.valueOf(Math.max(Integer.parseInt(params[0]), Integer.parseInt(params[1])));
            case "findmin":
                return String.valueOf(Math.min(Integer.parseInt(params[0]), Integer.parseInt(params[1])));
            case "isprime":
                return String.valueOf(isPrime(Integer.parseInt(params[0])));
            case "sin":
                return String.valueOf(Math.sin(Math.toRadians(Double.parseDouble(params[0]))));
            case "cos":
                return String.valueOf(Math.cos(Math.toRadians(Double.parseDouble(params[0]))));
            case "tan":
                return String.valueOf(Math.tan(Math.toRadians(Double.parseDouble(params[0]))));
            case "cot":
                return String.valueOf(1 / Math.tan(Math.toRadians(Double.parseDouble(params[0]))));
            case "sec":
                return String.valueOf(1 / Math.cos(Math.toRadians(Double.parseDouble(params[0]))));
            case "cosec":
                return String.valueOf(1 / Math.sin(Math.toRadians(Double.parseDouble(params[0]))));
            case "gcd":
                return String.valueOf(gcd(Integer.parseInt(params[0]), Integer.parseInt(params[1])));
            case "lcm":
                return String.valueOf(lcm(Integer.parseInt(params[0]), Integer.parseInt(params[1])));
            case "absolutevalue":
                return String.valueOf(Math.abs(Integer.parseInt(params[0])));
            case "ceil":
                return String.valueOf(Math.ceil(Double.parseDouble(params[0])));
            case "floor":
                return String.valueOf(Math.floor(Double.parseDouble(params[0])));
            case "round":
                return String.valueOf(Math.round(Double.parseDouble(params[0])));
            case "percentage":
                return String.valueOf((Double.parseDouble(params[0]) / Double.parseDouble(params[1])) * 100);
            case "areaofsquare":
                return String.valueOf(Math.pow(Double.parseDouble(params[0]), 2));
            case "areaofrectangle":
                return String.valueOf(Double.parseDouble(params[0]) * Double.parseDouble(params[1]));
            case "areaofcircle":
                return String.valueOf(Math.PI * Math.pow(Double.parseDouble(params[0]), 2));
            case "ispalindrome":
                return String.valueOf(isPalindrome(params[0]));
            case "absolutedifference":
                return String.valueOf(Math.abs(Integer.parseInt(params[0]) - Integer.parseInt(params[1])));
            case "ispositive":
                return String.valueOf(Integer.parseInt(params[0]) > 0);
            case "isperfectsquare":
                return String.valueOf(isPerfectSquare(Integer.parseInt(params[0])));
            case "cubeofdifference":
                return String.valueOf(Math.pow(Integer.parseInt(params[0]) - Integer.parseInt(params[1]), 3));
            case "averageofthree":
                return String.valueOf(
                        (Double.parseDouble(params[0]) + Double.parseDouble(params[1]) + Double.parseDouble(params[2]))
                                / 3);
            case "ismultiple":
                return String.valueOf(Integer.parseInt(params[0]) % Integer.parseInt(params[1]) == 0);
            case "sumofdigits":
                return String.valueOf(sumOfDigits(Integer.parseInt(params[0])));
            case "sumofsquares":
                return String.valueOf(sumOfSquares(Integer.parseInt(params[0])));
            case "reciprocal":
                if (Double.parseDouble(params[0]) == 0)
                    throw new ArithmeticException("Error: Division by zero.");
                return String.valueOf(1 / Double.parseDouble(params[0]));
            case "mean":
                return String.valueOf((Double.parseDouble(params[0]) + Double.parseDouble(params[1])) / 2);
            case "reversenumber":
                return String.valueOf(reverseNumber(Integer.parseInt(params[0])));
            case "degreestoradians":
                return String.valueOf(Math.toRadians(Double.parseDouble(params[0])));
            case "radianstodegrees":
                return String.valueOf(Math.toDegrees(Double.parseDouble(params[0])));
            case "maxofthree":
                return String.valueOf(Math.max(Integer.parseInt(params[0]),
                        Math.max(Integer.parseInt(params[1]), Integer.parseInt(params[2]))));
            case "minofthree":
                return String.valueOf(Math.min(Integer.parseInt(params[0]),
                        Math.min(Integer.parseInt(params[1]), Integer.parseInt(params[2]))));
            case "averageof3":
                return String.valueOf(
                        (Double.parseDouble(params[0]) + Double.parseDouble(params[1]) + Double.parseDouble(params[2]))
                                / 3);
            case "cubeofdiff":
                if (params.length != 2) {
                    throw new IllegalArgumentException("Error: cubeofdiff requires 2 arguments.");
                } else {
                    double a = Double.parseDouble(params[0].trim());
                    double b = Double.parseDouble(params[1].trim());
                    double diff = a - b;
                    return String.valueOf(Math.pow(diff, 3));
                }

            default:
                throw new Exception("Function implementation not available.");
        }
    }

    // Function to check if a number is a perfect square
    private boolean isPerfectSquare(int num) {
        int sqrt = (int) Math.sqrt(num);
        return sqrt * sqrt == num;
    }

    // Function to calculate the sum of digits of a number
    private int sumOfDigits(int num) {
        int sum = 0;
        while (num != 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    // Function to calculate the sum of squares of digits of a number
    private int sumOfSquares(int num) {
        int sum = 0;
        while (num != 0) {
            int digit = num % 10;
            sum += digit * digit;
            num /= 10;
        }
        return sum;
    }

    // Function to reverse a number, handling negative numbers
    private int reverseNumber(int num) {
        boolean isNegative = num < 0;
        num = Math.abs(num);

        int reversed = 0;
        while (num != 0) {
            reversed = reversed * 10 + num % 10;
            num /= 10;
        }

        return isNegative ? -reversed : reversed;
    }

    // Function to check if a number is prime
    private boolean isPrime(int num) {
        if (num <= 1)
            return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0)
                return false;
        }
        return true;
    }

    // Function to compute Greatest Common Divisor (GCD)
    private int gcd(int a, int b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    // Function to compute Least Common Multiple (LCM)
    private int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    // Function to check if a string is a palindrome
    private boolean isPalindrome(String str) {
        String reversed = new StringBuilder(str).reverse().toString();
        return str.equals(reversed);
    }

    private void generateTAC() {
        String functionDeclaration = editorTextArea.getText().trim();
        if (functionDeclaration.isEmpty()) {
            outputTextArea.setText("Error: No function to generate TAC from.");
            return;
        }

        // Extract function name and parameters
        int openParenIndex = functionDeclaration.indexOf("(");
        int closeParenIndex = functionDeclaration.indexOf(")");

        if (openParenIndex == -1 || closeParenIndex == -1 || closeParenIndex < openParenIndex) {
            outputTextArea.setText("Error: Invalid function format.");
            return;
        }

        String functionName = functionDeclaration.substring(0, openParenIndex).trim().toLowerCase();
        String paramsString = functionDeclaration.substring(openParenIndex + 1, closeParenIndex);
        String[] params = paramsString.split(",");
        StringBuilder tac = new StringBuilder();

        try {
            switch (functionName) {
                case "add":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = t1 + t2\n");
                    tac.append("result = t3\n");
                    break;

                case "subtract":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = t1 - t2\n");
                    tac.append("result = t3\n");
                    break;

                case "multiply":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = t1 * t2\n");
                    tac.append("result = t3\n");
                    break;

                case "divide":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = t1 / t2\n");
                    tac.append("result = t3\n");
                    break;

                case "modulus":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = t1 % t2\n");
                    tac.append("result = t3\n");
                    break;

                case "power":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = pow(t1, t2)\n");
                    tac.append("result = t3\n");
                    break;

                case "squareroot":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = sqrt(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "cuberoot":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = cbrt(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "nthroot":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = pow(t1, 1 / t2)\n");
                    tac.append("result = t3\n");
                    break;

                case "iseven":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = t1 % 2\n");
                    tac.append("t3 = t2 == 0\n");
                    tac.append("result = t3\n");
                    break;

                case "isodd":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = t1 % 2\n");
                    tac.append("t3 = t2 != 0\n");
                    tac.append("result = t3\n");
                    break;

                case "half":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = t1 / 2\n");
                    tac.append("result = t2\n");
                    break;

                case "double":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = t1 * 2\n");
                    tac.append("result = t2\n");
                    break;

                case "increment":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = t1 + 1\n");
                    tac.append("result = t2\n");
                    break;

                case "decrement":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = t1 - 1\n");
                    tac.append("result = t2\n");
                    break;

                case "max":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = (t1 > t2) ? t1 : t2\n");
                    tac.append("result = t3\n");
                    break;

                case "min":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = (t1 < t2) ? t1 : t2\n");
                    tac.append("result = t3\n");
                    break;

                case "isprime":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = is_prime(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "sin":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = sin(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "cos":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = cos(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "tan":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = tan(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "cot":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = tan(t1)\n");
                    tac.append("t3 = 1 / t2\n");
                    tac.append("result = t3\n");
                    break;

                case "sec":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = cos(t1)\n");
                    tac.append("t3 = 1 / t2\n");
                    tac.append("result = t3\n");
                    break;

                case "cosec":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = sin(t1)\n");
                    tac.append("t3 = 1 / t2\n");
                    tac.append("result = t3\n");
                    break;

                case "gcd":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = gcd(t1, t2)\n");
                    tac.append("result = t3\n");
                    break;

                case "lcm":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = lcm(t1, t2)\n");
                    tac.append("result = t3\n");
                    break;

                case "abs":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = abs(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "ceil":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ceil(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "floor":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = floor(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "round":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = round(t1)\n");
                    tac.append("result = t2\n");
                    break;

                case "absdiff":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = abs(t1 - t2)\n");
                    tac.append("result = t3\n");
                    break;

                case "ispositive":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = t1 > 0\n");
                    tac.append("result = t2\n");
                    break;

                case "isperfectsquare":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = sqrt(t1)\n");
                    tac.append("t3 = t2 * t2\n");
                    tac.append("t4 = t3 == t1\n");
                    tac.append("result = t4\n");
                    break;

                case "cubeofdiff":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = t1 - t2\n");
                    tac.append("t4 = t3 * t3 * t3\n");
                    tac.append("result = t4\n");
                    break;

                case "average3":
                    tac.append("t1 = ").append(params[0].trim()).append("\n");
                    tac.append("t2 = ").append(params[1].trim()).append("\n");
                    tac.append("t3 = ").append(params[2].trim()).append("\n");
                    tac.append("t4 = t1 + t2 + t3\n");
                    tac.append("t5 = t4 / 3\n");
                    tac.append("result = t5\n");
                    break;

                case "ismultiple":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = " + params[1].trim() + "\n");
                    tac.append("t3 = t1 % t2 == 0\n");
                    tac.append("result = t3\n");
                    break;

                // sum of digits
                case "sumdigits":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = sum_of_digits(t1)\n");
                    tac.append("result = t2\n");
                    break;

                // sum of squares
                case "sumsquares":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = sum_of_squares(t1)\n");
                    tac.append("result = t2\n");
                    break;

                // reciprocal
                case "reciprocal":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = 1 / t1\n");
                    tac.append("result = t2\n");
                    break;

                // mean of n (assuming passed as array)
                case "mean":
                    tac.append("t1 = sum(" + params[0].trim() + ")\n");
                    tac.append("t2 = length(" + params[0].trim() + ")\n");
                    tac.append("t3 = t1 / t2\n");
                    tac.append("result = t3\n");
                    break;

                // reverse number
                case "reverse":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = reverse(t1)\n");
                    tac.append("result = t2\n");
                    break;

                // degrees to radians
                case "deg2rad":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = t1 * (3.1416 / 180)\n");
                    tac.append("result = t2\n");
                    break;

                // radians to degrees
                case "rad2deg":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = t1 * (180 / 3.1416)\n");
                    tac.append("result = t2\n");
                    break;

                // percentage
                case "percentage":
                    tac.append("t1 = " + params[0].trim() + "\n"); // value
                    tac.append("t2 = " + params[1].trim() + "\n"); // total
                    tac.append("t3 = (t1 / t2) * 100\n");
                    tac.append("result = t3\n");
                    break;

                // area of square
                case "area_square":
                    tac.append("t1 = " + params[0].trim() + "\n"); // side
                    tac.append("t2 = t1 * t1\n");
                    tac.append("result = t2\n");
                    break;

                // area of rectangle
                case "area_rectangle":
                    tac.append("t1 = " + params[0].trim() + "\n"); // length
                    tac.append("t2 = " + params[1].trim() + "\n"); // width
                    tac.append("t3 = t1 * t2\n");
                    tac.append("result = t3\n");
                    break;

                // area of circle
                case "area_circle":
                    tac.append("t1 = " + params[0].trim() + "\n"); // radius
                    tac.append("t2 = 3.1416 * t1 * t1\n");
                    tac.append("result = t2\n");
                    break;

                // max of 3
                case "max3":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = " + params[1].trim() + "\n");
                    tac.append("t3 = " + params[2].trim() + "\n");
                    tac.append("t4 = max(t1, t2)\n");
                    tac.append("t5 = max(t4, t3)\n");
                    tac.append("result = t5\n");
                    break;

                // min of 3
                case "min3":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = " + params[1].trim() + "\n");
                    tac.append("t3 = " + params[2].trim() + "\n");
                    tac.append("t4 = min(t1, t2)\n");
                    tac.append("t5 = min(t4, t3)\n");
                    tac.append("result = t5\n");
                    break;

                // is palindrome
                case "ispalindrome":
                    tac.append("t1 = " + params[0].trim() + "\n");
                    tac.append("t2 = reverse(t1)\n");
                    tac.append("t3 = t1 == t2\n");
                    tac.append("result = t3\n");
                    break;

                default:
                    tac.append("Error: Unsupported function '").append(functionName).append("'\n");
                    break;
            }
        } catch (Exception e) {
            tac.setLength(0);
            tac.append("Error: Invalid parameters.\n");
        }

        outputTextArea.setText(tac.toString());
    }
}

class Function {
    String name;
    String identifiers;
    String dataTypes;
    int paramCount;
    String scope;
    int size;
    String returnType;

    public Function(String name, String identifiers, String dataTypes, int paramCount, String scope, int size,
            String returnType) {
        this.name = name;
        this.identifiers = identifiers;
        this.dataTypes = dataTypes;
        this.paramCount = paramCount;
        this.scope = scope;
        this.size = size;
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return String.format("| %-21s | %-20s | %-26s | %-16d | %-7s | %-4d | %-24s |",
                name, identifiers, dataTypes, paramCount, scope, size, returnType);
    }

    public static class FunctionTable {
        public static void main(String[] args) {
            List<Function> functions = new ArrayList<>();

            // Add functions to the list
            functions.add(new Function("Add", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Subtract", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Multiply", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Divide", "a, b", "double, double", 2, "Global", 16, "double"));
            functions.add(new Function("Modulus", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Power", "base, exponent", "double, double", 2, "Global", 16, "double"));
            functions.add(new Function("Squareroot", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Cuberoot", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Nth root", "a, n", "double, double", 2, "Global", 16, "double"));
            functions.add(new Function("Is even", "a", "int", 1, "Global", 4, "bool"));
            functions.add(new Function("Is odd", "a", "int", 1, "Global", 4, "bool"));
            functions.add(new Function("Half value", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Double value", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Increment", "a", "int", 1, "Global", 4, "int"));
            functions.add(new Function("Decrement", "a", "int", 1, "Global", 4, "int"));
            functions.add(new Function("Find max", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Find min", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Is prime", "a", "int", 1, "Global", 4, "bool"));
            functions.add(new Function("Sin", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Cos", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Tan", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Cot", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Sec", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Cosec", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Gcd", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Lcm", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Absolute value", "a", "int", 1, "Global", 4, "int"));
            functions.add(new Function("Ceil", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Floor", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Round", "a", "double", 1, "Global", 8, "int"));
            functions.add(new Function("Absolute difference", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Is positive", "a", "int", 1, "Global", 4, "bool"));
            functions.add(new Function("Is perfect square", "a", "int", 1, "Global", 4, "bool"));
            functions.add(new Function("Cube of diff", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Average of 3", "a, b, c", "int, int, int", 3, "Global", 12, "double"));
            functions.add(new Function("Is multiple", "a, b", "int, int", 2, "Global", 8, "bool"));
            functions.add(new Function("Sum of digits", "a", "int", 1, "Global", 4, "int"));
            functions.add(new Function("Sum of squares", "a, b", "int, int", 2, "Global", 8, "int"));
            functions.add(new Function("Reciprocal", "a", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Mean", "arr[], size", "int[], int", 2, "Global", 8 + 4, "double"));
            functions.add(new Function("Reverse number", "a", "int", 1, "Global", 4, "int"));
            functions.add(new Function("Degrees to radians", "deg", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Radians to degree", "rad", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Percentage", "a, b", "double, double", 2, "Global", 16, "double"));
            functions.add(new Function("Area of square", "side", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Area of Rectangle", "l, b", "double, double", 2, "Global", 16, "double"));
            functions.add(new Function("Area of circle", "radius", "double", 1, "Global", 8, "double"));
            functions.add(new Function("Max of three", "a, b, c", "int, int, int", 3, "Global", 12, "int"));
            functions.add(new Function("Min of three", "a, b, c", "int, int, int", 3, "Global", 12, "int"));
            functions.add(new Function("Is palindrome", "a", "int", 1, "Global", 4, "bool"));

            // Print header
            System.out.println(
                    "| Function Name         | Identifiers          | Data Type(s)               | Parameter Count  | Scope   | Size | Attributes (Return Type)  |");
            System.out.println(
                    "|-----------------------|----------------------|----------------------------|------------------|---------|------|---------------------------|");

            // Print each function in the table
            for (Function function : functions) {
                System.out.println(function);
            }
        }
    }
}
