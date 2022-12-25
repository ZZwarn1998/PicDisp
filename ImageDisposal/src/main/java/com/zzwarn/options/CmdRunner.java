package com.zzwarn.options;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class CmdRunner {
    // Please be sure to determine if both of py_interpreter_loc and py_project_loc are right in your computer.
    // Absolute path of python interpreter (Recommend Anaconda virtual environment)
    final String py_interpreter_loc = "D:\\Anaconda3\\envs\\projenv\\python.exe";
    // Absolute path of python project, p\PicDisposal_py
    final String py_project_loc = "D:\\PCproj\\PicDisposal_py\\cmd.py";

    CmdRunner(){}

    public CmdRunner(String type) {
        try {
            System.out.println(Paths.get(this.getClass().getResource("/").toURI()));
        } catch (URISyntaxException e){
            System.out.println(e.toString());
        }
        String[] say = new String[]{"Use", type};
    }

    public static void runCommand(String[] cmd) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(cmd);
        System.out.println("here!");
        StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
        errorGobbler.start();
        StreamGobbler outGobbler = new StreamGobbler(proc.getInputStream(), "STDOUT");
        outGobbler.start();
        proc.waitFor();

    }

    public String getPy_interpreter_loc(){
        return this.py_interpreter_loc;
    }

    public String getPy_project_loc(){
        return this.py_project_loc;
    }
}
