package com.zzwarn.disposals;

import com.zzwarn.options.CmdRunner;

import java.io.File;
import java.io.IOException;

public class Emboss {
    public static void dispose(String img_path, String dir_save) throws IOException, InterruptedException {
        CmdRunner cmdRunner = new CmdRunner(new String("Emboss"));
        String[] command = new String[] { cmdRunner.getPy_interpreter_loc(), cmdRunner.getPy_project_loc(),
                "emboss", "--path", img_path, "--dir_save", dir_save};
        CmdRunner.runCommand(command);
    }
}
