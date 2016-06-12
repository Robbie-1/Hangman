import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

class Hangman_2 {
	
	/**
	 * @author Robbie McLeod
	 */
	
	static String UI, randomWord;
	static Scanner scanner = new Scanner(System.in);
	static JPanel dragPanel = new JPanel( new DragLayout() );
	static JFrame frame = new JFrame();
	static JLabel startIMG;
	static JLabel[] fields = new JLabel[10];
	static byte totalGuesses, currentHMSprite, currentHM, currentChar, currentWord = 0;
	static boolean customList;
	static String rwLTU = ""; //randomWordLengthToUnderscore
	static String textProgress = rwLTU;
	static ImageIcon[] imgSTORE = {new ImageIcon("hangman_images/0.gif"), new ImageIcon("hangman_images/1.gif"), new ImageIcon("hangman_images/2.gif"), new ImageIcon("hangman_images/3.gif"), new ImageIcon("hangman_images/4.gif"), new ImageIcon("hangman_images/5.gif"), new ImageIcon("hangman_images/6.gif"), new ImageIcon("hangman_images/7.gif"), new ImageIcon("hangman_images/8.gif"), new ImageIcon("hangman_images/9.gif"), new ImageIcon("hangman_images/10.gif")};
	//static String[] wordsList = {"lime", "orange", "grape"};
	static ArrayList<String> wordsList = new ArrayList<>();
	static ArrayList<String> userWordsList = new ArrayList<>();
    static ArrayList<String> goodGuesses = new ArrayList<>();
	static ArrayList<String> badGuesses = new ArrayList<>();
	
	public static void main(String args[]) {
		init();
	}
	
	public static void init() {
		loadFrame();
		packDefaults();
        loadDefaults();
        loadFrameComponents();
        sendStartup();
        startGuessProcess();
	}
        
	public static void loadDefaults() {
		pullRandomWord();
		convertString(randomWord);
		textProgress = rwLTU;
	}
	
	public static void packDefaults() {
		if (userWordsList.size() >= 1) {
			reload();
		}
		wordsList.add(0, "lime");
		wordsList.add(1, "orange");
		wordsList.add(2, "grape");
	}
	
	public static void reload() {
		UI = "";
		scanner = new Scanner(System.in);
		dragPanel = new JPanel( new DragLayout() );
		startIMG = new JLabel();
		fields = new JLabel[10];
		totalGuesses = 0;
	    goodGuesses = new ArrayList<>();
		badGuesses = new ArrayList<>();
		currentHMSprite = 0;
		currentHM = 0;
		randomWord = "";
		currentChar = 0;
		rwLTU = "";
		textProgress = rwLTU;
		currentWord = 0;
		drawNewHM();
	}
	
    public static void loadFrame() {
        frame.setSize(460, 366);
        frame.setLocationRelativeTo(null); //Centre's the frame
       //frame.setResizable(false);
        frame.setTitle("A453 Project - Hangman Game");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public static void loadFrameComponents() {
    	frame.add( new JScrollPane(dragPanel) );
    	startIMG = new JLabel(imgSTORE[currentHM]);
        JLabel text = new JLabel("Hangman Game");
        text.setLocation(175, 275);
        JLabel text2 = new JLabel("by Robbie McLeod");
        text2.setLocation(170, 290);
    	dragPanel.add(startIMG);
        dragPanel.add(text);
        dragPanel.add(text2);
        frame.revalidate(); //refresh
    	
      //frame.getContentPane().add(new JLabel(imgSTORE[0])); //Index: [0]
        //fields[1] = new JLabel("<html><body><br><br>Some JLabel Text =]</body></html>");
        //frame.add(text);
        //frame.add(fields[1]);
    }
    
    public static void sendStartup() {
        //System.out.println("[DEBUG]: Random word: "+randomWord+"; Length: "+randomWord.length()+"\n");
        System.out.println("[A453 Programming Project - Task Three: Hangman]");
        sleep(2);
        System.out.println("[Game]: A random word has been generated!");
        sleep(2);
        //System.out.println("[Game]: Random word: "+rwLTU+" : LENGTH: "+rwLTU.length());
        System.out.println("[Game]: Random word: "+stringFix(rwLTU));
        sleep(2);
    }
    
    public static String stringFix(String strIN) {
    	String newStr = "";
    	for (int i = 0; i < strIN.length(); i++) {
    		newStr += " __ ";
    	}
    	rwLTU = newStr;
    	return newStr;
    }
    
    public static String convertString(String txt) {
    	/**
    	 * "rwLTU" means "randomWordLengthToUnderline"
    	 */
    	rwLTU="";
    	for (int i = 0; i < txt.length(); i++) {
    		rwLTU += "_";
    	}
    	return rwLTU;
    }
    
    public static String pullRandomWord() {
    	Random rand = new Random();
    	
    	/**
    	 * Ternary operator usage for efficiency.
    	 */
    	return randomWord = (customList) ? userWordsList.get(rand.nextInt(userWordsList.size())) : wordsList.get(rand.nextInt(wordsList.size()));
    }
    
    public static void startGuessProcess() {
    	handleResponse(pollUI());
    }
    
    public static String pollUI() {
        if (goodGuesses.size() == randomWord.length()) {
            System.out.println("Congratulations, you've identified the word!");
            System.out.println("Score: "+(6-badGuesses.size()));
    		handleRestart(newWords());
        } 
    	totalGuesses++;
    	System.out.println("Guess a letter.");
    	UI = scanner.nextLine();
    	return UI;
    }
    
    public static void handleResponse(String response) {
    	if (response.length() == 0 || response.length() > 1) {
    		System.out.println("You must only guess one character at a time!");
    		startGuessProcess();
    	} else {
    		char c = response.charAt(0); //Since the user response will only be one character in length, this will not cause an error.
    		updateUI(c);
    	}
    }
    
    public static void updateUI(char charEntered) {
    	boolean hasUpperCase = String.valueOf(charEntered).equals(String.valueOf(charEntered).toLowerCase());
    	if (hasUpperCase == false) {
    		System.out.println("[Game]: Please refrain from entering upper-case characters.");
    		startGuessProcess();
    		return;
    	}
    	if (randomWord.contains(String.valueOf(charEntered))) {
    		//addNewLetter(charEntered);
    		convertString(randomWord);
    		updateLetters(charEntered);
    	} else {
            if (badGuesses.contains(String.valueOf(charEntered))) {
                    System.out.println("[Game]: You've already guessed this letter!");
                    startGuessProcess();
                    return;
            }
            badGuesses.add(String.valueOf(charEntered)); //Casted to String.
            checkGuesses(pollBadGuesses());
            drawNewHM();
    	}
    }
    
    /*public static boolean checkIfMoreLetters(String strIN) {
        int count = 0;
        for (int i=0; i < randomWord.length(); i++) {
            if (String.valueOf(randomWord.charAt(i)).equals(strIN)) {
                count++;
            } else {
                continue;
            }
        }
        if (count > 0) {
            System.out.println("does have more of the same char");
            return true;
        } else {
            System.out.println("does NOT have more of the same char");
            return false;
        }
    }*/
    
    public static void updateLetters(char newLetter) {
        if (randomWord.contains(String.valueOf(newLetter))) {
            if (goodGuesses.contains(String.valueOf(newLetter))) {
                System.out.println("[Game]: You've already guessed this letter!");
                startGuessProcess();
                return;
            }
            
            //orange
            //r
            //index: 1
            int idx = randomWord.indexOf(newLetter); //1
            String before = textProgress.substring(0, idx); //0 -- 1
            int remainder = (rwLTU.length() - 1); //6 - 1 = 5
            String after = textProgress.substring((idx+1), (remainder+1)); //1 -- 5
            //BEFORE: 0-1
            //AFTER:  1-5
            textProgress = (before+newLetter+after);
            System.out.println("[Game]: Correct guess, well done!");
            goodGuesses.add(String.valueOf(newLetter));
            System.out.println("[Game]: Good guesses: "+goodGuesses.toString());
            System.out.println("[Game]: Bad guesses: "+badGuesses.toString());
        }
    	System.out.println(textProgress);
    	startGuessProcess();
    }
    
    /*public static void addNewLetter(char charEntered) {
    	if (currentChar >= 1) {
    		//dragPanel.remove(component);
    		//dragPanel.add(component);
    	} else {
    		JLabel alpha = new JLabel(alphabetSTORE[(pullIntegerFromAlpha(String.valueOf(charEntered)) - 1)]); 
    		alpha.setLocation(getPosX(charEntered), 325);
    		dragPanel.add(alpha);
    	}
    }*/
    
    /*public static int getPosX(char charEntered) {
    	// A P P L E
    	// 0 1 2 3 4
    	int charPositionInWord = randomWord.indexOf(charEntered); //e.g. 3
    	System.out.println("char pos in word: "+charPositionInWord);
    	
    	int width = frame.getWidth();
    	System.out.println("frame width:"+width);
    	// 3/4 = ((3 divided by 4) * 100)
    	/*int posX = (int) ((charPositionInWord / width) * 100000);*/
    	/*int posX = ((charPositionInWord/100) * (width * 100));
    	System.out.println("posX: "+posX);
    	//3 / 460 * 100
    	
    	return posX;
    }*/
    
	/**
	 * Pulls the correct integer value based on the character entered by the user in their guess.
	 */
    public static int pullIntegerFromAlpha(String str) {
    	str = str.toLowerCase(); //Prevents case error.
    	char[] ls = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();
    	Map<Character, Integer> m = new HashMap<Character, Integer>();
    	int j = 1;
    	for(char c : ls) {
    		m.put(c, j++);
    	}
    	int i = 0;
    	int mul = 1;
    	for(char c: new StringBuffer(str).reverse().toString().toCharArray()) {
    		i += m.get(c) * mul;
    		mul *= ls.length;
    	}
    	return i;
    }
    
    public static void checkGuesses(String str) {
    	if (badGuesses.size() == 6) {
    		System.out.println("//GAME OVER//\n//You've run out of lives!!!//");
    		handleRestart(newWords());
    	} else {
    		System.out.println("[BadGuesses]: "+str);
    		startGuessProcess();
    	}
    }
    
    public static boolean newWords() {
    	System.out.println("Would you like to start a new game? ");
    	UI = scanner.nextLine();
    	if (UI.equalsIgnoreCase("yes") || UI.equalsIgnoreCase("y")) {
    		System.out.println("Great!\nWould you like to enter your own set of words for someone else to guess?");
    		UI = scanner.nextLine();
    		if (UI.equalsIgnoreCase("yes") || UI.equalsIgnoreCase("y")) {
    			customList = true;
    			return true;
    		} else {
    			System.out.println("Restarting program...\n");
    			customList = false;
    			sleep(2);
    			return false;
    		}
    	} else {
    		System.out.println("OK! Thanks for playing!");
    		sleep(1);
    		System.exit(0);
    	}
    	return false;
    }
    
    public static void handleRestart(boolean newWordSet) {
    	if (newWordSet == true) {
    		loopWords();
    		customList = true;
    	}
    }
    
	/*
	 * Restarts the game.
	 */
    public static void restartProgram() {
    	frame.dispose();
		sleep(2);
		init();
    }
    
	public static byte grabID() {
        currentWord++;
        return currentWord;
	}
    
	public static void loopWords() {
		System.out.println("Please enter word "+grabID()+": ");
    	UI = scanner.nextLine();
		if (UI.equalsIgnoreCase("Done")) {
			System.out.println("Thanks for entering your new word set.\n"
					+ "The program will now restart using your new word list.\n");
			restartProgram();
		} else {
			userWordsList.add(UI);
			//userWordsList[grabID()] = UI;
			System.out.println("Words so far: "+userWordsList.toString());
			loopWords();
		}
	}
    
    public static String pollBadGuesses() {
    	int size = badGuesses.size();
    	String strToReturn;
    	switch (size) {
    		
    	case 1:
    		strToReturn = "You have 1 bad guess - "+"'"+badGuesses.get(0)+"'";
    	break;
    		
    	/**
    	 * Represents the protocol to use for all other occurrences.
    	 */
    	default:
    		strToReturn = "You have "+badGuesses.size()+" bad guesses - "+badGuesses.toString();
    	break;
    	
    	}
    	drawNewHM();
    	return strToReturn;
    }
    
    public static void drawNewHM() {
    	if (currentHM == 0) {
            currentHM++;
            dragPanel.remove(startIMG);
            dragPanel.add(new JLabel(imgSTORE[currentHM]));
            frame.revalidate(); //refresh
                //removes old, adds new.
    	} else {
            if (currentHM >= 7) {
                return;
            }
            currentHM++;
            dragPanel.remove(new JLabel(imgSTORE[(currentHM - 1)]));
            dragPanel.add(new JLabel(imgSTORE[currentHM]));
            frame.revalidate(); //refresh
    	}
    }
    
    public static void sleep(int period) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(period));
        } catch (InterruptedException ex) {
            Logger.getLogger(Hangman_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    /**
     * --> Get char index from randomWord based on the char entered by the user. (currentChar)
     * --> Change rwLTU in the right index to the char entered by the user. (n = 5n)
     * --> Print the edited rwLTU.
     */
    
    /*public static void updateUI(char UI) {
    	//int underscoreIndex = rwLTU.indexOf(((randomWord.indexOf(currentChar))));
    	//char charToReplace = rwLTU.charAt(underscoreIndex);
    	//each char in randomWord is equal to 5 char's in rwLTU.
    	for (int i=0; i < randomWord.length(); i++) {
    		if (randomWord.charAt(i) == UI) {
    			currentChar = randomWord.charAt(i);
    			//text.setText("<html><body>A453 Project - Hangman Game<br><br><br><br>"+newStr+"</body></html>");
    		} else {
    			continue;
    		}
    	}
    	System.out.println(randomWord.indexOf(currentChar));
    	//replaceUnderscore(rwLTU.indexOf(rwLTU.charAt(randomWord.indexOf(currentChar))), currentChar, randomWord.indexOf(currentChar));
    }*/
    
    /*public static void replaceUnderscore(int underscoreIndex, char character, int wordIndex) {
    	
    	//" ___ "
    	underscoreIndex = ((5 * wordIndex) + 2);

    	int idx = rwLTU.indexOf((underscoreIndex - 1));
    	String newStr = StringUtils.replaceOnce(rwLTU, String.valueOf(rwLTU.charAt(underscoreIndex)), String.valueOf(character)); <-- only replaces first seen underscore, rendering it useless.
    	//StringUtils.replaceOnce(rwLTU, String.valueOf(rwLTU.charAt(underscoreIndex)), String.valueOf(character));
    	
    	System.out.println(rwLTU.charAt(randomWord.indexOf(currentChar)));
    	System.out.println("Underscore Index: "+underscoreIndex+"; Character: "+character);
        text.setText("<html><body>A453 Project - Hangman Game<br><br><br><br>"+newStr+"</body></html>");
    }*/
    
    /*public static void updateUI(int position, char letter) {
    	int idx = rwLTU.indexOf("____");
    	char newChar = rwLTU.charAt(idx);
    	String newStr = rwLTU.replace(newChar, letter);
        text.setText("<html><body>A453 Project - Hangman Game<br><br><br><br>"+newStr+"</body></html>");
        System.out.println("UI updated.");
    }*/
    
}
