/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.sys;

import org.xxdevplus.sys.EventBlaster;
import java.util.ArrayList;
import org.xxdevplus.struct.KeyStore;
import org.xxdevplus.struct.Sorter;

/**
 *
 * @author GeTr
 */



public class SystemEventQueue
{

 private KeyStore<Double> m_EventsTimes               = new KeyStore<Double>(0, new Sorter(0));
 private KeyStore<Double> m_EventDelays               = new KeyStore<Double>(0, new Sorter(0));
 private KeyStore<String> m_EventsKeys                = new KeyStore<String>(0, new Sorter(0));
 private KeyStore<String> m_SourceNames               = new KeyStore<String>(0, new Sorter(0));
 private KeyStore<String> m_EventTypes                = new KeyStore<String>(0, new Sorter(0));

 private KeyStore<Object> m_Objects                   = new KeyStore<Object>(0, new Sorter(0));
 private KeyStore<Object> m_ParameterBlocks           = new KeyStore<Object>(0, new Sorter(0));


 //public Event SystemEvent(ByVal SourceName As String, ByVal EventType As String, ByVal Source As Object, ByVal Objects As ccdObjects, ByVal Parameters As ccdStrings)
 //public Event SysEvent(ByVal SourceName As String, ByVal EventType As String, ByVal Source As Object, ByVal Params As ccdParameterBlock, ByVal reserved As Variant)

 private KeyStore<String> mStringSet;
 private long lastModified;

 private String m_SourceName;
 private String m_EventType;
 private String m_Key;

 private EventBlaster m_EventBlaster = null;

 public                              SystemEventQueue       (                                      ) throws Exception
 {
  //Clear();
  m_EventBlaster = new EventBlaster();
  m_EventBlaster.Listener(this);
 }
 private void                        SourceName             (String                           value)                  { m_SourceName = value; m_Key = m_SourceName + ":" + m_EventType; }
 private void                        EventType              (String                           value)                  { m_EventType = value; m_Key = m_SourceName + ":" + m_EventType; }

 private void                        FireEvent(String SourceName, String EventType, Object Source, KeyStore<Object> Params, Object reserved)
 {
//  If appSys_CanTerminate Then Exit Sub
//  RaiseEvent SysEvent(SourceName, EventType, Source, Params, reserved)
 }

/*

Sub putEvent(ByVal SourceName As String, ByVal EventType As String, ByVal Source As Object, ByVal Params As ccdParameterBlock, ByVal reserved As Variant)
    FireEvent SourceName, EventType, Source, Params, reserved
End Sub


private void UpdateEventKey(ByVal Key As String, ByVal Delay As Double)
    If (Delay = 0) Then
       m_EventsTimes.Remove Key
       m_EventDelays.Remove Key
       m_EventsKeys.Remove Key
       m_SourceNames.Remove Key
       m_EventTypes.Remove Key
       m_Objects.Remove Key
       m_ParameterBlocks.Remove Key
'       m_ParametersColl.Remove Key
    Else
       m_EventDelays.Remove Key
       m_EventDelays.Add Delay, Key
       m_EventsTimes.Remove Key
       m_EventsTimes.Add timer, Key
    End If
End Sub

'Enters an Event Description into the Queue only if this Event Description is not already there
void postEvent(ByVal SourceName As String, ByVal EventType As String, ByVal Source As Object, ByVal Params As ccdParameterBlock, ByVal reserved As Variant, ByVal Milliseconds As Long, ByVal pulse As Boolean)

    Dim Key                 As String
    Dim Time                As Double
    Dim KeyFound            As Boolean

    Key = UCase(SourceName & ":" & EventType)
    On Error Resume Next
    Time = m_EventsTimes.Item(Key)
    KeyFound = (Err = 0)
    On Error GoTo 0: Err.Clear
    If KeyFound Then
       If (Milliseconds = 0) Then UpdateEventKey Key, 0
       If (Milliseconds < 50) Then Milliseconds = 50
       If pulse Then
          UpdateEventKey Key, -Milliseconds / 1000
       Else
          UpdateEventKey Key, Milliseconds / 1000
       End If
    Else
       m_EventsTimes.Add timer, Key
       If pulse Then
          m_EventDelays.Add -(Milliseconds / 1000), Key
       Else
          m_EventDelays.Add (Milliseconds / 1000), Key
       End If
       m_EventsKeys.Add Key, Key
       m_SourceNames.Add SourceName, Key
       m_EventTypes.Add EventType, Key
       m_Objects.SetItem Source, Key
       m_ParameterBlocks.Add Params, Key
'      m_ParametersColl.Add Parameters, Key
    End If

End Sub

void pulse()

    Dim EventName               As String
    Dim CommandName             As String
    Dim obj                     As Object
    Dim Objects                 As ccdParameterBlock
'    Dim Parameters              As ccdStrings

    Dim Key                 As Variant
    Dim SingleEventFound    As Boolean
    Static IdleLoopCounter  As Long

    SingleEventFound = False
    For Each Key In m_EventsKeys
        If (m_EventDelays(Key) > 0) Then SingleEventFound = True
        If (timer - m_EventsTimes(Key) > Abs(m_EventDelays(Key))) Then
           EventName = m_SourceNames(Key)
           CommandName = m_EventTypes(Key)
           Set obj = m_Objects(Key)
           Set Objects = m_ParameterBlocks(Key)
'           Set Parameters = m_ParametersColl(Key)
           If (m_EventDelays(Key) > 0) Then    'Single Event , not a Pulse Event
              m_EventsTimes.Remove Key
              m_EventDelays.Remove Key
              m_EventsKeys.Remove Key
              m_SourceNames.Remove Key
              m_EventTypes.Remove Key
              m_Objects.Remove Key
              m_ParameterBlocks.Remove Key
'              m_ParametersColl.Remove Key
           Else
              UpdateEventKey Key, m_EventDelays(Key)
           End If
           FireEvent EventName, CommandName, obj, Objects, ""
        End If
    Next Key
//    If SingleEventFound Then
//       IdleLoopCounter = 0
//    Else
//       IdleLoopCounter = IdleLoopCounter + 1
//       If (IdleLoopCounter > 1) Then
//          FireEvent "APPSYSTEM_IS_IDLE", "APPSYSTEM_IS_IDLE", AppSystem, AppSystem.ParameterBlock, ""
//          IdleLoopCounter = 0
//       End If
//    End If

End Sub


void Class_Terminate()
On Error Resume Next: Static Meth As String: If (Meth = "") Then Meth = "Class_Terminate" Else Exit Sub
    If Not (m_frmEventBlaster Is Nothing) Then
       Set m_frmEventBlaster.Listener = Nothing
       Unload m_frmEventBlaster
       Set m_frmEventBlaster = Nothing
    End If
EventCleanUp: InitTerminateDebug Me, False, Meth, Err: On Error GoTo 0: 'DEvents
End Sub

//Resets the Queue
void Clear()
    Set mStringSet = New ccdStringSet
End Sub

public Property Get This() As ccdSystemEventQueue: Set This = Me: End Property         'Use it in With-Clauses: example: With myObj: .Mode = 1: .Enabled = True: DoAnything .This: End With


 */

}
