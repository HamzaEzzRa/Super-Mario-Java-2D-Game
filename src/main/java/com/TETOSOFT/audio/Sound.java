package com.TETOSOFT.audio;

import java.io.*;
import javax.sound.sampled.*;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class Sound {
    private AudioFormat format;
    private AudioInputStream stream;
    private SourceDataLine line;
    private float rate;
    private String name;
    private String fullPath;
    private boolean paused;
    private boolean playing;
    private boolean ready;
    private boolean finished;
    private int pos;
    private int loops;
    private byte[] buffer;
    
    public static final int BUFFER_SIZE = 24 * 1024;
    public static final byte[] ZERO = new byte[BUFFER_SIZE];
    
    public static final String SOUND_PATH = "sounds/";
    
    public Sound() {
        name = null;
        fullPath = null;
        paused = false;
        playing = false;
        ready = false;
        loops = 0;
        pos = 0;
        buffer = new byte[BUFFER_SIZE];
        finished = false;
    }
    
    public Sound(String name) {
        this();
        load(name);
    }
    
    public Sound (String name, int loopCount) {
        this(name);
        setLoops(loopCount);
    }
    
    public void setLoops(int i) {
        // 0 ---> play once
        // -1 ---> loop forever
        loops = i;
    }

    public int getLoops() {
        return loops;
    }
    
    public String getName() {
        return name;
    }
    
    private synchronized void load(String name) {
        dispose();
        this.name = name;
        this.fullPath = SOUND_PATH + name;
        
        try {
            AudioInputStream source = AudioSystem.getAudioInputStream(new File(fullPath));
            format = getOutFormat(source.getFormat());
            stream = AudioSystem.getAudioInputStream(format, source);
        } catch (IOException | UnsupportedAudioFileException
            | IllegalArgumentException e) {
            throw new RuntimeException("Sound exception : " + e);
        }
        
        finished = false;
    }
    
    public synchronized void reload() {
        try {
            AudioInputStream source = AudioSystem.getAudioInputStream(new File(fullPath));
            format = getOutFormat(source.getFormat());
            stream = AudioSystem.getAudioInputStream(format, source);
        } catch (IOException | UnsupportedAudioFileException
            | IllegalArgumentException e) {
            throw new RuntimeException("Sound exception : " + e);
        }
        
        finished = false;
    }

    public synchronized void changePitch(float factor) {
        try {
            format = getOutFormat(rate * factor);
            stream = AudioSystem.getAudioInputStream(format, stream);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Sound exception : " + e);
        }
    }
    
    public synchronized void loadData() {
        pos = 0;
        if (name == null)
            return;
        if (finished==true)
            return;
        finished = !floodBuffer(buffer);
    }
    
    public synchronized boolean floodBuffer(byte[] buffer) {
        int offset = 0;
        int numBytesRead = 0;

        try {
            while (offset < BUFFER_SIZE) {
                numBytesRead = stream.read(buffer, offset, BUFFER_SIZE - offset);
                if (numBytesRead < 0) {
                    if (loops == 0) {
                        System.arraycopy(ZERO, offset, buffer, offset, BUFFER_SIZE - offset);
                        return false;
                    }
                    else {
                        reload();
                        if (loops > 0)
                            loops--;
                    }
                }
                else
                    offset += numBytesRead;
            }
        } catch (IOException e) {
            throw new RuntimeException("Sound exception : " + e);
        }
        
        return true;
    }
    
    public synchronized void dispose() {
        stream = null;
        line = null;
        name = null;
        paused = false;
        playing = false;
        loops = 0;
        pos = 0;
    }
    
    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isPaused() {
        line.stop();
        return paused;
    }

    public boolean isReady() {
        return ready;
    }
    
    public boolean play() {
        if (paused)
            return false;
        playing = true;

        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);
            line = (SourceDataLine)AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException e) {
            throw new RuntimeException("Sound exception : " + e);
        }

        loadData();
        line.start();
        ready = true;
        
        return true;
    }

    public void stop() {
      ready = false;
      playing = false;
      if (line != null) {
        line.drain();
        line.close();
      }
    }

    public boolean outputSound() {
        int stored;
        int temp;
        if (!playing || paused)
            return false;
        if (finished)
            playing = false;
        else {
            stored = line.available();
            if (stored + pos >= BUFFER_SIZE) {
                line.write(buffer, pos, BUFFER_SIZE - pos);
                temp = stored + pos - BUFFER_SIZE;
                while (temp > BUFFER_SIZE) {
                    temp -= BUFFER_SIZE;
                    loadData();
                    if (finished)
                        return true;
                    line.write(buffer, 0, BUFFER_SIZE);
                }
                loadData();
                if (finished)
                    return true;
                line.write(buffer, 0, temp);
                pos = temp;
            }
            else {
                line.write(buffer, pos, stored);
                pos += stored;
            }
            if (pos >= BUFFER_SIZE)
                loadData();
        }
        
        return true;
    }
    
    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }
    
    private AudioFormat getOutFormat(float customRate) {
        final int ch = format.getChannels();
        return new AudioFormat(PCM_SIGNED, customRate, 16, ch, ch * 2, rate, false);
    }
}
