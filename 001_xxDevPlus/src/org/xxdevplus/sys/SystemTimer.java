/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.sys;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author GeTr
 */
public class SystemTimer
{

 private boolean   enabled  = false;
 private long      interval = 0;
 private Timer     timer    = null;

 public   void     Enabled  (boolean value) { enabled = value; if (interval == 0) enabled = false; if (enabled) { timer = new Timer(); timer.schedule(new PulseTask(), interval);} else timer = null; }
 public   boolean  Enabled  (             ) { return enabled;  }

 public   void     Interval (long    value) { interval = value; if (interval == 0) Enabled(false);  }
 public   long     Interval (             ) { return interval;  }

 private class PulseTask extends TimerTask
 {
  public void run() { System.out.println("Timer"); }
 }

}
