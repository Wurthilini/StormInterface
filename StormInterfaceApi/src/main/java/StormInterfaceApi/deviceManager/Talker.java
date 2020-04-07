package StormInterfaceApi.deviceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.Mixer.Info;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

import StormInterfaceApi.utilities.StormInterfaceException; 

  
public class Talker { 
  
	//private Synthesizer synthesizer; 
	private AudioPlayer audioPlayer = null;
	private final String voiceName = "kevin16";
	private String buttonText;
	private VoiceManager voiceManager;
	private Voice voice;
	private File fileIn;
	
	public Talker(String button) throws Exception
	{
		this.voiceManager = VoiceManager.getInstance();
		this.voice = voiceManager.getVoice(this.voiceName);
		this.voice.allocate();
		if(this.voice==null)
			throw new StormInterfaceException("Cannot find voice named " + this.voiceName + ". Please specify a different voice.");
		switch(button)
		{
		case "Left":
			this.buttonText = "Left";
			break;
		case "Right":
			this.buttonText = "Right";
			break;
		case "Up":
			this.buttonText = "Up";
			break;
		case "Down":
			this.buttonText = "Down";
			break;
		case "JackIn":
			this.buttonText = "JackIn";
			break;
		case "JackOut":
			this.buttonText = "JackOut";
			break;
		case "testStormAudio":
			this.buttonText = "testStormAudio";
			break;
		default:
			throw new StormInterfaceException("Unknown Button Text: " + this.buttonText + ".");
		}
		saveFile();
	}
	
	private void saveFile() throws Exception
	{
		boolean waveFileAlreadyExists = new File("../StormInterfaceApi/audio/", this.buttonText + ".wav").exists();
		String outputDir = "../StormInterfaceApi/audio/" + this.buttonText;
		if(!waveFileAlreadyExists || this.buttonText != "testStormAudio")
		{
			this.audioPlayer = new SingleFileAudioPlayer(outputDir,Type.WAVE);
			this.voice.setAudioPlayer(audioPlayer);
			this.voice.speak(this.buttonText);
			this.voice.deallocate();
			this.audioPlayer.close();
		}
		this.fileIn = new File(outputDir + ".wav"); 
		loadWavFile();
	}
	
	private void loadWavFile() throws Exception
	{
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.fileIn);
		List<Mixer.Info> fittingSoundDevices = filterDevices();
		System.out.println(fittingSoundDevices.get(2));
		try
		{
			Clip line = AudioSystem.getClip(fittingSoundDevices.get(2));
			line.open(audioInputStream);
			line.start();
			line.drain();
			audioInputStream.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	private List<Mixer.Info> filterDevices() {
		Line.Info playbackLine = new Line.Info(SourceDataLine.class);
	    List<Mixer.Info> result = new ArrayList<Mixer.Info>();
	    Info[] infos = AudioSystem.getMixerInfo();
	    for (Mixer.Info info : infos) {
	        Mixer mixer = AudioSystem.getMixer(info);
	        if (mixer.isLineSupported(playbackLine)) {
	            result.add(info);
	        }
	    }
	    return result;
	}
} 