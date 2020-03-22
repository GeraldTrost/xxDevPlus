/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.sys;

import java.awt.event.MouseEvent;

/**
 *
 * @author GeTr
 */
public class EventInfo
{

 public MouseEvent cmouse = null;
 
 // -1 = must not be pressed   0 = need not be pressed /or/ released resp.   1 = must be pressed /or/ is pressed resp.
 public int NumLock       = 0;
 public int ScrollLock    = 0;
 public int CapsLock      = 0;
 public int Alt           = 0;
 public int AltGrey       = 0;
 public int Control       = 0;
 public int RightControl  = 0;
 public int Shift         = 0;
 public int RightShift    = 0;

 


}
