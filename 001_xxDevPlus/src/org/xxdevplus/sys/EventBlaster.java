/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.sys;

import org.xxdevplus.utl.utl;

/**
 *
 * @author GeTr
 */
public class EventBlaster
{

 private Object m_Listener;

 private       SystemEventQueue     Listener            (                                 )                  { return (SystemEventQueue) m_Listener; }
               void                 Listener            (SystemEventQueue            value)                  { m_Listener = value; utl.SysTimer.Enabled(m_Listener != null); }
 //private       void                 EventBlaster_Timer  (                                 )                  { try { if (m_Listener != null) Listener().pulse(); } catch(Exception ex) {}; }
 protected     void                 finalize            (                                 ) throws Throwable { utl.SysTimer.Enabled(false); super.finalize(); }


}
