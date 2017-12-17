import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
/**
 *  Takes a single command-line argument designating the file to open.
 *  Counts the frequencies of every word in the file, and outputs the number of words it found.
 *  Allows user to search for the count of individual words, the first and last words, and the predecessors and successors of specific words
 *  Also allows user to delete words, or quit the program.
 * 
 * @author Sara Wille 
 * @version 8 November 2017
 */
public class WordFreqs2
{
    public static void main(String[] args) {
        RedBlackTree<String, Integer> tree = new RedBlackTree<>();//create game
        try {
            //get file from args
            File myFile = null;
            if (args.length == 1) {
                myFile = new File(args[0]);
            }
            //scan word by word
            Scanner data = new Scanner(myFile);
            Scanner user = new Scanner(System.in);
            data.useDelimiter("'*[^a-zA-Z0-9_']+'*");
            //add words to tree
            while(data.hasNext()) {   
                String next = data.next();
                next = next.toLowerCase();

                if(!next.equals("")){
                    int freq = 0;
                    //System.out.println(next);
                    if(tree.get(next) != null){
                        freq = (int)tree.get(next);
                    }
                    freq++;
                    tree.put(next, freq);
                }
            }
            //print number of words
            tree.recalcRank();
            System.out.println("This text contains " + tree.size() + " distinct words.");
            System.out.println("Please enter a word to get its frequency, or hit enter to leave.");

            //look for user input
            String input = user.nextLine();
            while(!input.equals("")){

                if(input.startsWith("-")){
                    String newInput = input.substring(1);
                    if(tree.get(newInput)!=null){
                        tree.delete(newInput);
                        System.out.println("\"" + newInput + "\" has been deleted.");
                    }else{
                        System.out.println(newInput+" was not found.");
                    }
                }else if(input.startsWith("<")){
                    String newInput = input.substring(1);
                    if(newInput.equals("")){
                        System.out.println("The alphabetically-first word in the text is \"" + tree.getMinKey() + "\"");
                    }else{
                        String pred = tree.findPredecessor(newInput);
                        if(pred == null){
                            System.out.println("No words come before " + newInput);
                        }else{
                            System.out.println("The word \""+ pred +"\" comes before \"" +newInput + "\"");
                        }
                    }

                }else if(input.startsWith(">")){
                    String newInput = input.substring(1);
                    if(newInput.equals("")){
                        System.out.println("The alphabetically-last word in the text is \"" + tree.getMaxKey() + "\"");
                    }else{
                        String succ = tree.findSuccessor(newInput);
                        if(succ == null){
                            System.out.println("No words come after " + newInput);
                        }else{
                            System.out.println("The word \""+ succ +"\" comes after \"" +newInput + "\"");
                        }
                    }

                }else if(input.startsWith("&")){
                    String newInput = input.substring(1);
                    if(tree.get(newInput)!=null){
                        System.out.println("The rank of \"" + newInput + "\" is " +tree.findRank(newInput));
                    }else{
                        System.out.println(newInput+" was not found.");
                    }

                }else if(input.startsWith("*")){
                    int newInput = Integer.parseInt(input.substring(1));
                    System.out.println("The word at rank " + newInput + " is \"" +tree.select(newInput) + "\"");
                }
                else if(tree.get(input) == null){
                    System.out.println("\"" + input + "\" does not appear.");
                }else{
                    System.out.println("\"" + input + "\" appears " + tree.get(input) + " times.");
                }
                System.out.println("Please enter a word to get its frequency, or hit enter to leave.");
                input = user.nextLine();
            }
            System.out.println("Goodbye!");
            data.close();
            user.close();
        } catch (FileNotFoundException e) {
            System.err.println("File does not exist");
        }catch(NullPointerException e){
            System.err.println("You forgot to give me an argument");

        } catch (Exception e) {
            System.err.println("Oops, I've caught an exception: " + e);
            e.printStackTrace(System.err);
            //
        }
    }
}