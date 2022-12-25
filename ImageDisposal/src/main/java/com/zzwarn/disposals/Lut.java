package com.zzwarn.disposals;

import com.zzwarn.options.CmdRunner;

import java.io.File;
import java.io.IOException;

public class Lut {
    public static void dispose(String img_path, String dir_save, String lut_filename) throws IOException, InterruptedException {
        CmdRunner cmdRunner = new CmdRunner(new String("Lut"));
        String[] command = new String[]{cmdRunner.getPy_interpreter_loc(), cmdRunner.getPy_project_loc(),
                "lut", "--path", img_path, "--approach", lut_filename, "--dir_save", dir_save};
        CmdRunner.runCommand(command);
    }

}
