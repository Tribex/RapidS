/**
 * @file Provides functions for playing audio
 * @author Nateowami, Tribex
 */

require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.java.io.File);
require(Packages.java.util.Properties);
require(Packages.java.io.FileInputStream);
//WAV and PCM player.
require(Packages.javax.sound.sampled.AudioSystem);
require(Packages.javax.sound.sampled.Clip);
require(Packages.javax.sound.sampled.DataLine);
//OGG player
require(Packages.org.newdawn.easyogg.OggClip);
//MP3 player
require(Packages.javazoom.jl.player.Player);


/**
 * Plays a sound clip. Available formats: OGG, WAV, PCM, and MP3
 * Larger PCM and WAV files may not play, as they are not buffered.
 * @param filename {string} The path to the file you want to play.
 * @param format {string} Use this to force a music format. Options: OGG, WAV, PCM, and MP3
 */
audio.playClip = function(filename, format){

    //Set the format to the file ending if the format is not specified.
    if (format == null) {
        var splitFileName = filename.split(".");
        format = splitFileName[splitFileName.length-1].toUpperCase();
    }

    //Play a WAV or PCM file.
    if (format.toUpperCase() == "WAV" || format.toUpperCase() == "PCM") {
        var sound = AudioSystem.getAudioInputStream(new File(Globals.getCWD(filename)));

        //This fixes an error on Ubuntu.
        var info = new DataLine.Info(Clip, sound.getFormat());
        var clip = AudioSystem.getLine(info);
        clip.open(sound);
        clip.start();

        //Play an OGG file.
    } else if (format.toUpperCase() == "OGG") {
        try {
            var ogg = new OggClip(Globals.getCWD(filename));
            ogg.play();
        } catch (e) {
            program.showError(e.message)
        }

        //Play an MP3 file.
    } else if (format.toUpperCase() == "MP3") {
        try{
            var fis = new FileInputStream(Globals.getCWD(filename));
            var player = new Player(fis);
            player.play();
        } catch(e){
            program.showError(e.message);
        }
    }
};

