/**
 * @file Contains audio playback functionality.
 * @author Tribex
 */
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.javax.sound.sampled.AudioSystem);
/**
 * Returns an audio stream that can be stopped, played, and paused.
 */
audio.openFile = function(file) {
    var audioObject = {};
    audioObject.file = file;
    audioObject.playSound = function() {
        try {
            var clip = AudioSystem.getClip();
            var inputStream = AudioSystem.getAudioInputStream(new File(Globals.getCWD(audioObject.file)));
            clip.open(inputStream);
            clip.start();
        } catch (e) {
            console.log(e.message);
        }
    }
    return audioObject;
}
