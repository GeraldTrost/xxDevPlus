/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.struct;

import java.util.ArrayList;

/**
 *
 * @author GeTr
 */
public class ParameterBlock

{

 
 /*
private KeyList<Object> m_Objects                 = null;
private KeyList<Object> m_TerminateCallBack       = null;
        KeyList<Object>            Objects      (                     )                  { return m_Objects; }
        void                       Objects      (KeyList<Object> value)                  { m_Objects = value; }
        String[]                   Keys         (                     ) throws Exception { return m_Objects.kAsc(); }
        void                       Clear        (                     ) throws Exception { m_Objects = new KeyList<Object>(0, new Sorter(0)); m_TerminateCallBack = new KeyList<Object>(0, new Sorter(0)); } //m_Objects.WeakMode = True
public  boolean                    Contains     (String            Key) throws Exception { return m_Objects.hasKey(Key); }


public void Remove(ByVal Key As String)
   m_Objects.Remove Key
End Sub

public void SetItem(ByVal Item As Variant, ByVal Key As String)

   If m_Objects.Contains(Key) Then m_Objects.Remove Key

   If IsObject(Item) Then
      Dim obj As Object
      Set obj = Item
      Select Case True
      Case (Key = "TerminateCallBack"):
           If Not m_TerminateCallBack.Contains(ObjPtr(obj)) Then
              m_TerminateCallBack.Add obj, ObjPtr(obj)
              AppSystem.AppUserLongs("Term" & ObjPtr(obj)).Add ObjPtr(Me), ObjPtr(Me)
           End If
      Case Else: m_Objects.Add obj, Key
      End Select
   Else
      Dim Cont As New ccdContainerForSimpleTypes
      Cont.Value = Item
      m_Objects.Add Cont, Key
   End If

End Sub

public Property Get Item(ByVal Key As String) As Variant
   Dim ForceObject As Boolean:         ForceObject = (Left(Key, 11) = "OBJFROMPTR:")
   Dim RetObj As Object:               If ForceObject Then Set RetObj = m_Objects(Mid(Key, 12)) Else Set RetObj = m_Objects(Key)
   Select Case TypeName(RetObj)
   Case "ccdContainerForSimpleTypes":  Dim Cont As ccdContainerForSimpleTypes: Set Cont = RetObj: If ForceObject Then Set Item = ObjectFromPointer(Cont.Value) Else Item = Cont.Value
   Case Else:                          Set Item = RetObj
   End Select
End Property

public ParameterBlock Clone()

   Dim RetObj As New ccdParameterBlock
   Set Clone = RetObj
   Set RetObj.Objects = m_Objects.Clone

End Function

private void Class_Initialize()
On Error GoTo EventCleanUp: Static Meth As String: If (Meth = "") Then Meth = InitTerminateDebug(Me, True, "Class_Initialize") Else Exit Sub
   Clear
EventCleanUp: On Error GoTo 0: Meth = ""
End Sub

private void Class_Terminate()
On Error Resume Next: Static Meth As String: If (Meth = "") Then Meth = "Class_Terminate" Else Exit Sub

//   If Hex(ObjPtr(Me)) = "783F660" Then Stop

   Dim obj          As Object
   For Each obj In m_TerminateCallBack
       AppSystem.AppUserLongs("Term" & ObjPtr(obj)).Remove ObjPtr(Me)
       If (AppSystem.AppUserLongs("Term" & ObjPtr(obj)).Count = 0) Then
//          Select Case TypeName(obj)
//          Case "ccdMultiAlignBox":
//               Dim maBox As ccdMultiAlignBox: Set maBox = obj:
//               'Stop
//               'maBox.TerminateCallBack Me
//          Case Else:                 obj.TerminateCallBack Me
//          End Select
          //AppSystem.BroadCastEvent TypeName(obj), "TerminateCallBack", obj, , , 300
          Debug.Print "TerminateCallBack BROADCAST " & "Source=" & Me.Item("Source")
          AppSystem.BroadCastEvent TypeName(obj) & "-" & Hex(ObjPtr(obj)), "TerminateCallBack", obj
       Else
          Debug.Print "TerminateCallBack buffered " & "Source=" & Me.Item("Source")
       End If

   Next obj

   Set m_Objects = Nothing               'AttGeTr: kommt wieder weg, wer wei, was das Ding verursacht .....
   Set m_TerminateCallBack = Nothing     'AttGeTr: kommt wieder weg, wer wei, was das Ding verursacht .....

EventCleanUp: InitTerminateDebug Me, False, Meth, Err: On Error GoTo 0: 'DEvents
End Sub

//      Case "String"
//      Case "Long"
//      Case "Integer"
//      Case "Boolean"
//      Case "Double"
//      Case "Single"
//      Case Else: Err.Raise 555, "Invalid Type in ParameterPackage.PutItem """ & TypeName(Item) & """"
//      End Select


public Property Get This() As ccdParameterBlock: Set This = Me: End Property         'Use it in With-Clauses: example: With myObj: .Mode = 1: .Enabled = True: DoAnything .This: End With





 */


}
