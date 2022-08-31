package br.com.r90dev.compressormp4;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegProgress;
import com.github.kokorin.jaffree.ffmpeg.NullOutput;
import com.github.kokorin.jaffree.ffmpeg.ProgressListener;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import java.io.File;


public class CortaFFmpeg {
    void split(String pathToFile, int partes) throws Exception
        {
            
            
            Path    pathToFFmpeg    = FileSystems.getDefault().getPath(System.getProperty("user.dir") + "/ffmpeg");
            
            String  filename        = pathToFile.substring(pathToFile.lastIndexOf("\\")).replace("\\", "");
            String  videoDir        = pathToFile.replace(filename, "");
            long    outputDuration  = 0;
            long    inputDuration   = 0;
            File splitFile = new File(videoDir+"/OUTPUT");//Destination folder to save.
            if (!splitFile.exists()) {
                splitFile.mkdirs();
            }
            //
            // Get input duration
            final AtomicLong durationMillis = new AtomicLong();

            FFmpeg.atPath(pathToFFmpeg)
            .addInput(UrlInput.fromUrl( pathToFile ))
            .addOutput(new NullOutput())
            .setProgressListener(
                    new ProgressListener()
                    {
                        @Override
                        public void onProgress(FFmpegProgress progress)
                        {
                            durationMillis.set(progress.getTimeMillis());
                        }
                    }
                )
            .execute();

            inputDuration = durationMillis.get();
            outputDuration =(inputDuration/partes) + ((inputDuration%partes == 0) ? 0 : 1);
            
            System.out.println(filename + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>> DURAÇÃO: " + inputDuration + " milliseconds / DIVIDINDO EM VÍDEOS DE " + outputDuration);
            
            // Split Video
            long currPoint = 0;
            
            for(int n=0; n < partes; n++)
            {
                long remaining = inputDuration - ( outputDuration * n );
                
                long currOutputDuration = remaining > outputDuration ? outputDuration : remaining;
                
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Video " + (n+1) + ": " + currPoint + " / " + currOutputDuration);
                
                FFmpeg.atPath(pathToFFmpeg)
                .addInput(
                        UrlInput.fromUrl( videoDir + filename )
                        .setPosition(currPoint, TimeUnit.MILLISECONDS)
                        .setDuration(currOutputDuration, TimeUnit.MILLISECONDS)
                        )
                .addOutput(
                        UrlOutput.toPath(FileSystems.getDefault().getPath( splitFile.getAbsolutePath()+"/" + filename + "_PARTE_" + n + ".mp4" ))
                        .setPosition(0, TimeUnit.MILLISECONDS)
                        .setDuration(currOutputDuration, TimeUnit.MILLISECONDS)
                        )
                .setOverwriteOutput(true)
                .execute();
                
                currPoint += outputDuration;
            }
        }
}
