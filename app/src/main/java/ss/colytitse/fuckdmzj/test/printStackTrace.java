package ss.colytitse.fuckdmzj.test;

import static ss.colytitse.fuckdmzj.hook.Others.*;
import android.util.Log;
public class printStackTrace {

    public static void setStackTracePrint(){
       StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        Log.d(TAG, "#############################[start]#############################");
        for (StackTraceElement stack :stackTraceElement){
            Log.d(TAG, "at " + stack.getClassName() + "." + stack.getMethodName() + "(" + stack.getFileName() + ":" + stack.getLineNumber());
        }
        Log.d(TAG, "#############################[end]#############################");

    }
}
