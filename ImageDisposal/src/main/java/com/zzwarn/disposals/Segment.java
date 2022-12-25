package com.zzwarn.disposals;

import com.zzwarn.options.CmdRunner;

import java.io.File;
import java.io.IOException;

public class Segment {
    public static void dispose(String img_path, String dir_save, String k) throws IOException, InterruptedException {
        CmdRunner cmdRunner = new CmdRunner(new String("Segment"));
        String[] command = new String[] { cmdRunner.getPy_interpreter_loc(), cmdRunner.getPy_project_loc(),
                "segment", "--path", img_path, "--k", k, "--dir_save", dir_save};
        CmdRunner.runCommand(command);
    }
}
