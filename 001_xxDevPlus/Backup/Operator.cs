using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ndString
{
 public class Operator
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Operator"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  public Reach Base = null;
  public Reach Part = null;

  internal Operator   (Reach Base)             {this.Base = Base; this.Part = Base;}
  internal Operator   (Reach Base, Reach Part) {this.Base = Base; this.Part = Part;}

// ******************* (Rch other)
  public Operator before      (Reach other)                                       {if (Part == null) Part = Base; return new Operator(Base, Part.before      (other));}
  public Operator upto        (Reach other)                                       {if (Part == null) Part = Base; return new Operator(Base, Part.upto        (other));}
  public Operator at          (Reach other)                                       {if (Part == null) Part = Base; return new Operator(Base, Part.at          (other));}
  public Operator from        (Reach other)                                       {if (Part == null) Part = Base; return new Operator(Base, Part.from        (other));}
  public Operator after       (Reach other)                                       {if (Part == null) Part = Base; return new Operator(Base, Part.after       (other));}
// ******************* (int pos) //supported: backward index (eInx < 0) where -1 == last Element
  public Operator before      (int  pos)                                          {if (Part == null) Part = Base; return new Operator(Base, Part.before      (pos));}
  public Operator upto        (int  pos)                                          {if (Part == null) Part = Base; return new Operator(Base, Part.upto        (pos));}
  public Operator at          (int  pos)                                          {if (Part == null) Part = Base; return new Operator(Base, Part.at          (pos));}
  public Operator from        (int  pos)                                          {if (Part == null) Part = Base; return new Operator(Base, Part.from        (pos));}
  public Operator after       (int  pos)                                          {if (Part == null) Part = Base; return new Operator(Base, Part.after       (pos));}
// ******************* (bool match, int occur, String chrs)
  public Operator before      (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.before      (match, occur, chrs));}
  public Operator upto        (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.upto        (match, occur, chrs));}
  public Operator at          (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.at          (match, occur, chrs));}
  public Operator from        (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.from        (match, occur, chrs));}
  public Operator after       (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.after       (match, occur, chrs));}
//
/*
  public Operator before      (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.before      (match, 1, chrs));}
  public Operator upto        (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.upto        (match, 1, chrs));}
  public Operator at          (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.at          (match, 1, chrs));}
  public Operator from        (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.from        (match, 1, chrs));}
  public Operator after       (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.after       (match, 1, chrs));}
*/
// ******************* (bool match, int occur, String chrs)
  public Operator Before      (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.Before      (match, occur, chrs));}
  public Operator Upto        (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.Upto        (match, occur, chrs));}
  public Operator At          (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.At          (match, occur, chrs));}
  public Operator From        (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.From        (match, occur, chrs));}
  public Operator After       (bool match, long occur, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.After       (match, occur, chrs));}
//
/*
  public Operator Before      (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.Before      (match, 1, chrs));}
  public Operator Upto        (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.Upto        (match, 1, chrs));}
  public Operator At          (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.At          (match, 1, chrs));}
  public Operator From        (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.From        (match, 1, chrs));}
  public Operator After       (            bool match, string chrs)               {if (Part == null) Part = Base; return new Operator(Base, Part.After       (match, 1, chrs));}
*/
// ******************* (int occur, String token)
  public Operator before      (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.before      (occur, token));}
  public Operator upto        (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.upto        (occur, token));}
  public Operator at          (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.at          (occur, token));}
  public Operator from        (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.from        (occur, token));}
  public Operator after       (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.after       (occur, token));}
//
  public Operator before      (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.before      (1, token));}
  public Operator upto        (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.upto        (1, token));}
  public Operator at          (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.at          (1, token));}
  public Operator from        (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.from        (1, token));}
  public Operator after       (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.after       (1, token));}
// ******************* (int occur, String token)
  public Operator Before      (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.Before      (occur, token));}
  public Operator Upto        (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.Upto        (occur, token));}
  public Operator At          (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.At          (occur, token));}
  public Operator From        (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.From        (occur, token));}
  public Operator After       (long occur,             string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.After       (occur, token));}
//
  public Operator Before      (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.Before      (1, token));}
  public Operator Upto        (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.Upto        (1, token));}
  public Operator At          (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.At          (1, token));}
  public Operator From        (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.From        (1, token));}
  public Operator After       (                        string token)              {if (Part == null) Part = Base; return new Operator(Base, Part.After       (1, token));}
// ******************* (int occur, bool prio, String... tokens)
  public Operator before      (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.before      (occur, prio, token));}
  public Operator upto        (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.upto        (occur, prio, token));}
  public Operator at          (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.at          (occur, prio, token));}
  public Operator from        (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.from        (occur, prio, token));}
  public Operator after       (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.after       (occur, prio, token));}
//
  public Operator before      (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.before      (1, prio, token));}
  public Operator upto        (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.upto        (1, prio, token));}
  public Operator at          (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.at          (1, prio, token));}
  public Operator from        (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.from        (1, prio, token));}
  public Operator after       (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.after       (1, prio, token));}
// ******************* (int occur, bool prio, String... tokens)
  public Operator Before      (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.Before      (occur, prio, token));}
  public Operator Upto        (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.Upto        (occur, prio, token));}
  public Operator At          (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.At          (occur, prio, token));}
  public Operator From        (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.From        (occur, prio, token));}
  public Operator After       (long occur,             bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.After       (occur, prio, token));}
//
  public Operator Before      (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.Before      (1, prio, token));}
  public Operator Upto        (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.Upto        (1, prio, token));}
  public Operator At          (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.At          (1, prio, token));}
  public Operator From        (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.From        (1, prio, token));}
  public Operator After       (                        bool prio, params string[] token)     {if (Part == null) Part = Base; return new Operator(Base, Part.After       (1, prio, token));}
//

  // a.take(a.before("xx").after(".")).swap("uuuu").Value;
  // a.take(a.before("xx").after(".")).swap("uuuu").Part;
  
  public Reach swap(string txt)          {return Base.before(Part) + new Reach(txt) + Base.after(Part);}
  public Reach swap(Reach txt)           {return Base.before(Part) + txt + Base.after(Part);}
  public Reach swap()                    {return Base.before(Part) + Base.after(Part);}

  public Reach Swap(string txt) 
  {
   Base._buf()._del().at(Part);
   Base._buf()._ins(txt).before(Part);
   return Base;
  }
    
  public Reach Swap(Reach txt)           {return null; }
  public Reach Swap()                    {return null; }

  public Reach SWAP(string txt)          {return null; }
  public Reach SWAP(Reach txt)           {return null; }
  public Reach SWAP()                    {return null; }

 }
}
