/**
 * @file Provides functions for playing audio
 * @author Nateowami
 */

require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.java.io.File);
require(Packages.javax.sound.sampled.AudioInputStream);
require(Packages.javax.sound.sampled.AudioSystem);
require(Packages.javax.sound.sampled.Clip);
require(Packages.javax.sound.sampled.DataLine);

/**
 * Plays a sound clip. If the clip specified is too long errors may occur. A clip is not buffered
 * and therefore cannot play a large file. TODO
 * @param nameAndURL {string} The name and URL of the sound you want to play
 */
audio.playClip = function(nameAndURL){
    var sound = AudioSystem.getAudioInputStream(new File(Globalg.getCWD(nameAndURL)));

    //This fixes an error on Ubuntu.
    var info = new DataLine.Info(Clip, sound.getFormat());
    var clip = AudioSystem.getLine(info);
    clip.open(sound);
    clip.start();
};
