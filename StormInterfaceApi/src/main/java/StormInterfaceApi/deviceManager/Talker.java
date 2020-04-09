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

import StormInterfaceApi.StormCommunicationManager;
import StormInterfaceApi.utilities.DeviceInfo;
import StormInterfaceApi.utilities.StormInterfaceException; 

  
public class Talker { 
  
	//private Synthesizer synthesizer; 
	private AudioPlayer audioPlayer = null;
	private final String voiceName = "kevin16";
	private String buttonText;
	private VoiceManager voiceManager;
	private Voice voice;
	private File fileIn;
	private JACKSTATUS jackStatus;
	private DeviceInfo deviceInfo = new DeviceInfo();
	private StormCommunicationManager stormCommunicationManager;
	
	public enum JACKSTATUS
	{
		ON,
		OFF;
	}
	
	public Talker(StormCommunicationManager stormCommunicationManager) throws StormInterfaceException
	{
		this.stormCommunicationManager = stormCommunicationManager;
		try {
			this.stormCommunicationManager.getDeviceStatus(this.deviceInfo);
		} catch (Exception e) {
			throw new StormInterfaceException("Unable to get DeviceInfo");
		}
		this.jackStatus = deviceInfo.getJackStatus() ? JACKSTATUS.ON : JACKSTATUS.OFF;
	    System.out.println(deviceInfo.getSerialNumber());
	    System.out.println(deviceInfo.getVersion());
	    System.out.println(deviceInfo.getJackStatus());
	    for(byte bytes : deviceInfo.getKeyCode())
	    	System.out.printf("%02x ", bytes);
	    System.out.println();
	}
	
	protected void setTalkerJackStatus(JACKSTATUS jackstatus)
	{
		this.jackStatus = jackstatus;
	}
	
	protected void closeTalker()
	{
		//TODO löscht noch nicht die kürzlich hinzugefügten
		File file = new File("../StormInterfaceApi/audio/");
		File[] contents = file.listFiles();
		for(File files : contents)
			if(files.getName()!="testStormAudio.wav")
			{
				try
				{
					files.delete();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		
	}
	
	protected void talk(String button) throws Exception
	{
		this.buttonText = button;
		saveFile();
	}
	
	private void saveFile() throws Exception
	{
		this.voiceManager = VoiceManager.getInstance();
		this.voice = voiceManager.getVoice(this.voiceName);
		this.voice.allocate();
		if(this.voice==null)
			throw new StormInterfaceException("Cannot find voice named " + this.voiceName + ". Please specify a different voice.");
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
		try
		{
			Clip line;
			//TODO saubere Lösung
			if(this.jackStatus == JACKSTATUS.OFF)
				line = AudioSystem.getClip(fittingSoundDevices.get(0));
			else
				line = AudioSystem.getClip(fittingSoundDevices.get(2));
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