import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.sampled.*;


class Song {
    private String title;
    private File file;

    public Song(String title, File file) {
        this.title = title;
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public File getFile() {
        return file;
    }
}

class MusicPlayer {
    private ArrayList<Song> album;
    private ArrayList<Song> playlist;
    private int currentSongIndex;
    private Clip clip;

    String RESET = "\u001B[0m";
    String RED = "\u001B[31m";
    String GREEN = "\u001B[32m";
    String YELLOW = "\u001B[33m";

    public MusicPlayer() {
        album = new ArrayList<>();
        playlist = new ArrayList<>();
        currentSongIndex = 0;
    }

    public void addSongToAlbum(Song song) {
        album.add(song);
    }

    public void addSongToPlaylist(int songNumber) {

        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
        if (songNumber >= 1 && songNumber <= album.size()) {
            Song song = album.get(songNumber - 1);
            if (!playlist.contains(song)) {
                playlist.add(song);
                System.out.println(GREEN +"Added to playlist: " + song.getTitle()+ RESET);
            } else {
                System.out.println(RED+"Song is already in the playlist."+RESET);
            }
        }
         
        else {
            System.out.println(RED+"Invalid song number."+RESET);
        }
    }
    
    public void deleteSongFromPlaylist(){
        
        while (true) {
            if (!playlist.isEmpty()) {
                System.out.println(YELLOW+"Current Playlist:"+RESET);
                for (int i = 0; i < playlist.size(); i++) {
                    System.out.println((i + 1) + ". " + playlist.get(i).getTitle());
                }

                Scanner scanner = new Scanner(System.in);
                System.out.println(YELLOW+"Enter song number to delete from playlist, or 'q' to go back to the menu:"+RESET);

                if (scanner.hasNextInt()) {
                    int selectedSongNumber = scanner.nextInt();
                    for (int i = 0; i < 10; i++) {
                        System.out.println();
                    }

                    if (selectedSongNumber >= 1 && selectedSongNumber <= playlist.size()) {
                        Song song = playlist.remove(selectedSongNumber - 1);
                        System.out.println(GREEN + "Removed from playlist: " + song.getTitle() + RESET);
                        break;
                    } else {
                        System.out.println(RED + "Invalid song number. Please try again." + RESET);
                    }
                } else {
                    String userInput = scanner.next();
                    for (int i = 0; i < 10; i++) {
                        System.out.println();
                    }
                    if (userInput.equalsIgnoreCase("q")) {
                        System.out.println(GREEN + "Returned to the menu." + RESET);
                        return;
                    } else {
                        System.out.println(RED + "Invalid input. Please enter a valid song number or 'q' to go back to the menu." + RESET);
                    }
                }
            } else {
                System.out.println(RED + "Playlist is empty. Returned to the menu." + RESET);
                return; 
            }
        }
    }



    public void playSong() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        
         for (int i = 0; i < 20; i++) {
            System.out.println();
        }

        if (!playlist.isEmpty()) {
            System.out.println(YELLOW+"Current Playlist:"+RESET);
            for (int i = 0; i < playlist.size(); i++) {
                System.out.println((i + 1) + ". " + playlist.get(i).getTitle());
            }
            while (true) {
                System.out.println(YELLOW+"Enter song number to play, or 'q' to go back to the menu:"+RESET);
                Scanner scanner = new Scanner(System.in);
                if (scanner.hasNextInt()) {
                    int songNumber = scanner.nextInt();
                    for (int i = 0; i < 20; i++) {
                    System.out.println();
                    }       
                    if (songNumber >= 1 && songNumber <= playlist.size()) {
                        currentSongIndex = songNumber - 1;
                        if (clip != null && clip.isRunning()) {
                            clip.stop();
                        }
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(playlist.get(currentSongIndex).getFile());
                        clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                        System.out.println(GREEN+"Playing: " + playlist.get(currentSongIndex).getTitle()+RESET);
                        break;
                    } else {
                        System.out.println(RED+"Invalid song number. Please try again."+RESET);
                        System.out.println(YELLOW+"Current Playlist:"+RESET);
                        for (int i = 0; i < playlist.size(); i++) {
                        System.out.println((i + 1) + ". " + playlist.get(i).getTitle());
                            }
                    }
                } else {
                    String next = scanner.next();
                    if (next.equalsIgnoreCase("q")) {
                        for (int i = 0; i < 20; i++) {
                        System.out.println();
                        }
                        break;
                    } else {
                        for (int i = 0; i < 20; i++) {
                        System.out.println();
                        }
                        System.out.println(RED+"Invalid input. Please try again."+RESET);
                        System.out.println(YELLOW+"Current Playlist:"+RESET);
                        for (int i = 0; i < playlist.size(); i++) {
                        System.out.println((i + 1) + ". " + playlist.get(i).getTitle());
                        }
                    }
                }
            }
        } else {
            System.out.println(RED+"Playlist is empty. Please add songs to your playlist."+RESET);
        }
    }

    public void nextSong() throws LineUnavailableException, IOException, UnsupportedAudioFileException {

        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
        if (!playlist.isEmpty()) {
            if (currentSongIndex < playlist.size() - 1) {
                currentSongIndex++;
            } else {
                currentSongIndex = 0;
            }

            if (clip != null && clip.isRunning()) {
                clip.stop();
            }

            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(playlist.get(currentSongIndex).getFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                System.out.println(GREEN+"Playing: " + playlist.get(currentSongIndex).getTitle()+RESET);
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(RED+"Playlist is empty. Please add songs to your playlist."+RESET);
        }
    }

    public void previousSong() {

        if (!playlist.isEmpty()) {
            if (currentSongIndex > 0) {
                currentSongIndex--;
                if (clip != null && clip.isRunning()) {
                    clip.stop();
                }
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(playlist.get(currentSongIndex).getFile());
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    System.out.println(GREEN + "Playing: " + playlist.get(currentSongIndex).getTitle() + RESET);
                } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            } else {
                currentSongIndex = playlist.size() - 1;
                if (clip != null && clip.isRunning()) {
                    clip.stop();
                }
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(playlist.get(currentSongIndex).getFile());
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    System.out.println(GREEN + "Playing: " + playlist.get(currentSongIndex).getTitle() + RESET);
                } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println(RED + "Playlist is empty. Please add songs to your playlist." + RESET);
        }
    }


    public void stopSong() {

        if (clip != null && clip.isRunning()) {
            clip.stop();
            System.out.println(GREEN + "Stopped playing." + RESET);
        } else {
            System.out.println(RED + "No song is currently playing." + RESET);
        }
    }


    public void resetPlaylist() {

            playlist.clear();
            currentSongIndex = 0;
            System.out.println(GREEN+"Playlist has been reset."+RESET);
    }

    public void resetSong() {

            for (int i = 0; i < 20; i++) {
                System.out.println();
            }

            if (clip != null) {
                if (clip.isRunning()) {
                    clip.stop();
                    clip.setFramePosition(0);
                    clip.start();
                    System.out.println(GREEN+"Song has been reset to the beginning."+RESET);
                } else {
                    System.out.println(RED+"No song is currently playing."+RESET);
                }
            } else {
                System.out.println(RED+"No song is currently playing."+RESET);
            }
        }

    public void displayAlbum(int pageNumber) {

        final int pageSize = 5;
        if (!album.isEmpty()) {
            int start = (pageNumber - 1) * pageSize;
            if (start < album.size()) {
                System.out.println(YELLOW +"Album Page " + pageNumber + ":" + RESET);
                for (int i = 0; i < pageSize && start + i < album.size(); i++) {
                    System.out.println(+ (start + i + 1) + ". " + album.get(start + i).getTitle());
                }
            } else {
                System.out.println(RED+"Invalid page number."+RESET);
            }
        } else {
            System.out.println(RED+"Album is empty."+RESET);
        }
    }

    public void displayPlaylist() {

        final int pageSize = 5;
        int currentPage = 1;
        boolean exit = false;

        while (!exit) {
            if (!playlist.isEmpty()) {
                int start = (currentPage - 1) * pageSize;

                Scanner scanner = new Scanner(System.in);
                System.out.println(YELLOW + "Playlist Page " + currentPage + ":" + RESET);
                for (int i = 0; i < pageSize && start + i < playlist.size(); i++) {
                    System.out.println((start + i + 1) + ". " + playlist.get(start + i).getTitle());
                }
                System.out.println(YELLOW + "Enter 'n' for next page, 'p' for previous page, or 'q' to go back to the menu:" + RESET);

                String next = scanner.next();
                for (int i = 0; i < 20; i++) {
                    System.out.println();
                }

                if (next.equalsIgnoreCase("n")) {
                    if (start + pageSize < playlist.size()) {
                        currentPage++;
                    } else {
                        System.out.println(RED + "\n\n\n\n\n\n\n\nNo more pages. Please enter 'p' to go back or 'q' to go to the menu." + RESET);
                    }
                } else if (next.equalsIgnoreCase("p")) {
                    currentPage = Math.max(1, currentPage - 1);
                } else if (next.equalsIgnoreCase("q")) {
                    exit = true;
                } else {
                    System.out.println(RED + "Invalid input. Please enter 'n' for next page, 'p' for previous page, or 'q' to go back to the menu." + RESET);
                }
            } else {
                System.out.println(RED + "Playlist is empty." + RESET);
                exit = true;
            }
        }
    }


}


public class Main {
    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String YELLOW = "\u001B[33m";
        MusicPlayer player = new MusicPlayer();
        Scanner scanner = new Scanner(System.in);
        int command;
        

        String[] songTitles = {
            "Cassandra - Kiss",
            "Adele - Easy On Me",
            "Ed Sheeran - Shivers",
            "The Kid Laroi, Justin Bieber - STAY",
            "Glass Animals - Heat Waves",
            "Ed Sheeran - Bad Habits",
            "Justin Bieber - Ghost",
            "Post Malone, Swae Lee - Sunflower",
            "Dua Lipa, DaBaby - Levitating",
            "Adele - Oh My God",
            "Kane Brown, H.E.R. - Blessed & Free",
            "NEIKED, Mae Muller, POLO G - Better Days",
            "Maroon 5, Megan Thee Stallion - Beautiful Mistakes",
            "The Weeknd - Blinding Lights",
            "Tones and I - Dance Monkey",
            "Taylor Swift - Wildest Dreams (Taylor's Version)",
            "OneRepublic - Sunshine",
            "AJR - Bang!",
            "Billie Eilish - bad guy",
            "Kane Brown, blackbear - Memory",
            "Skylar Grey, POLO G, Mozzy, Eminem - Last One Standing",
            "Justin Bieber, Daniel Caesar, Giveon - Peaches",
            "Powfu, beabadoobee - death bed (coffee for your head)",
            "24kgoldn, Iann Dior - Mood",
            "Bruno Mars, Anderson .Paak, Silk Sonic - Leave The Door Open",
            "Dua Lipa - Don't Start Now",
            "Keith Urban, P!nk - One Too Many",
            "G-Eazy, Marc E. Bassy - Faithful",
            "Kane Brown, Swae Lee, Khalid - Be Like That",
            "BTS - Dynamite",
            "Ed Sheeran - Overpass Graffiti",
            "Jonas Brothers - Who's In Your Head",
            "Shawn Mendes - It'll Be Okay",
            "Alesso, Katy Perry - When I'm Gone",
            "Sam Feldt, Rita Ora - Follow Me",
            "24kgoldn - More Than Friends",
            "Sadie Jean - WYD Now",
            "Ruel - GROWING UP IS",
            "Cheat Codes, Lee Brice, Lindsay Ell - How Do You Love",
            "Kygo, X Ambassadors - Undeniable",
            "JESSIA - But I Don'T",
            "Ed_Sheeran - I Don't Care feat. Justin Bieber",
            "Connor Price - Straight A's",
            "Ed Sheeran - Beautiful People ft Khalid",
        };

        for (String title : songTitles) {

            File file = new File("music/" + title + ".wav");
            if (file.exists()) {
                player.addSongToAlbum(new Song(title, file));
            } else {
                System.out.println(RED+"File does not exist: " + file.getPath()+RESET);
            }
        }

        int currentPage = 1;
        while (true) {
            System.out.println(YELLOW+"------------------------------------"+RESET);
            System.out.println(YELLOW+"      GROUP 6'S MUSIC PLAYER"+RESET);
            System.out.println(YELLOW+"------------------------------------"+RESET);
            System.out.println("1. Add song to playlist");
            System.out.println("2. Delete song from playlist");
            System.out.println("3. Play selected song");
            System.out.println("4. Play next song");
            System.out.println("5. Play previous song");
            System.out.println("6. Stop song");
            System.out.println("7. Reset song");
            System.out.println("8. Reset playlist");
            System.out.println("9. Display playlist");
            System.out.println("0. Exit");
            System.out.print(YELLOW+"Enter your choice: "+RESET); 

            if (scanner.hasNextInt()) {  
            command = scanner.nextInt();    
            for (int i = 0; i < 20; i++) {
                System.out.println();
            }
            switch (command) {
                case 1:
                    while (true) {
                        player.displayAlbum(currentPage);
                        System.out.println(YELLOW + "Enter song number to add to playlist, 'n' for next page, 'p' for previous page, or 'q' to go back to the menu:" + RESET);
                        if (scanner.hasNextInt()) {
                            int songNumber = scanner.nextInt();
                            player.addSongToPlaylist(songNumber);
                        } else {
                            String next = scanner.next();
                            if (next.equalsIgnoreCase("n")) {
                                for (int i = 0; i < 20; i++) {
                                    System.out.println();
                                }
                                currentPage++;
                            } else if (next.equalsIgnoreCase("p")) {
                                currentPage = Math.max(1, currentPage - 1);
                                for (int i = 0; i < 20; i++) {
                                    System.out.println();
                                }
                            } else if (next.equalsIgnoreCase("q")) {
                                for (int i = 0; i < 20; i++) {
                                    System.out.println();
                                }
                                break;
                            } else {
                                for (int i = 0; i < 20; i++) {
                                    System.out.println();
                                }
                                System.out.println(RED + "Invalid input. Please enter a valid command." + RESET);
                            }
                        }
                    }
                    break;
                case 2:
                    player.deleteSongFromPlaylist();
                    break;
                case 3:
                    player.playSong();
                    break;

                case 4:
                    player.nextSong();
                    break;
                case 5:
                    player.previousSong();
                    break;
                case 6:
                    player.stopSong();
                    break;
                case 7:
                    player.resetSong();
                    break;
                case 8:
                    player.resetPlaylist();
                    break;
                case 9:
                    player.displayPlaylist();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println(RED+"Invalid command."+RESET);
            }
        }else {
                String invalidCommand = scanner.next();
                for (int i = 0; i < 20; i++) {
                    System.out.println();
                }
                System.out.println(RED + "Invalid command: " + invalidCommand + RESET);
            }
        }
    }
}

