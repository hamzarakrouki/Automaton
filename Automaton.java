/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MAChamza
 */
import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class Automaton {

	    // l'ensemble de tous les 'etats
	    Set<State> states;
	    // l'etat initial
	    State      initial;
	    // les 'etats acceptants
	    Set<State> accept;
	    
	    /** cr'ee un automate qui ne reconnait rien du tout
	     */
	    Automaton() {
	        initial  = new State();
	        states   = new TreeSet<State>();
	        accept   = new TreeSet<State>();
	        states.add(initial);
	    }

	    
	    /** cr'ee un automate avec ces parametres
	     */
	    Automaton(Set<State> _states, State _initial, Set<State> _accept) {
		states  = _states;
		initial = _initial;
		accept  = _accept;
		assert (states.contains(initial) && states.containsAll(accept));
	    }
            static Automaton debut(Automaton a, int i,char alphabet,int j)
	{
		State n=a.initial;
		a.initial.id=i;
		
		
		State l=new State(j);
		n.addTransition(alphabet,l);
		a.states.add(l);
	
	
	/*if(tabaccept.contains(a.initial.id))a.accept.add(f);
	if(tabaccept.contains(n.id))a.accept.add(n);*/
	return a;
		
	}

	    /** cr'ee un automate qui reconnait un mot donn'e
	     */
	    static Automaton new_WORD(String word) {
		Automaton  a = new Automaton();
		State      f = a.initial;
		for (int i=0; i<word.length(); i++) {
		    State n = new State();
		    a.states.add(n);
		    f.addTransition(word.charAt(i), n);
		    f = n;
		}
		a.accept.add(f);
		return a;
	    }

	    /** cr'ee un automate qui reconnait le mot vide
	     */
	    static Automaton new_EMPTY() {
		return new_WORD("");
	    }
	    
	    /** cr'ee un automate qui reconnait un Charactere donn'ee
	     */
	    static Automaton new_CHAR(char c) {
		return new_WORD(""+c);
	    }
	    
	    /** cr'ee un automate qui reconnait n'importe quel Charactere (mais exactement un seul)
	     */
	    static Automaton new_WILD()  {
		Automaton  a = new Automaton();
		State      f = new State(); // 'etat final acceptant
		a.accept.add(f);
		a.states.add(f);
		for (char c=0; c<256; c++)
		    a.initial.addTransition(c,f);
		return a;
	    }

	    /** cr'ee un automate qui reconnait l'union des mots reconnus par les 2 automates donn'ees
	     */
	    static Automaton new_OR(Automaton a1, Automaton a2) {
		Set<State> states = new TreeSet<State>();
		states.addAll(a1.states);
		states.addAll(a2.states);
		
		
		Set<State> accept = new TreeSet<State>();
		accept.addAll(a1.accept);
		accept.addAll(a2.accept);

		State      i = new State();
		
		i.addTransition(a1.initial);
	    i.addTransition(a2.initial);
	    states.add(i);
		return new Automaton(states, i, accept);
	    }

	    /** cr'ee un automate qui reconnait la concatenation des mots reconnus par les 2 automates donn'ees
	     */
	    static Automaton new_SEQ(Automaton a1, Automaton a2) {
		Set<State> states = new TreeSet<State>();
		states.addAll(a1.states);
		states.addAll(a2.states);

		for (State f: a1.accept)
		    f.addTransition(a2.initial);
		
		return new Automaton(states, a1.initial, a2.accept);
	    }

	    /** cr'ee un automate qui reconnait la repetition de ce que reconnait a
	     */
	    static Automaton new_STAR(Automaton a) {
		a.accept.add(a.initial);

		for (State f: a.accept)
		    f.addTransition(a.initial);
		
		return a;
	    }


	    /** teste si un mot donne est accept'e
	     */
	    boolean matches(String word) {
		// initially the current state contains only
		// the initial state and all epsilon transition reachable states
	        Set<State> current = new TreeSet<State>();
	        current.add(initial);
		State.epsilon_closure(current);
		// loop over all letters in the word
	        for (int i=0; i<word.length(); i++) {
		    Set<State> next = new TreeSet<State>();
		    dump(word, i, current);
	            char c = word.charAt(i);
	            for (State s : current) {
			// where does Character c lead us on the state s?
			State n = s.transition[c];
			if (n!=null) {
			    next.add(n);
			}
		    }
	            current = next;
		    State.epsilon_closure(current);
	        }
		dump(word, word.length(), current);
		// is one of the current states accepting ?
		for (State f: current) 
		    if (accept.contains(f))
			return true;
		return false;
	    }


	    /** affiche l'automate comme un graphe en notation DOT
	        ... enfin retourne une chaine qui correspond a l'affichage plutot
	    */
	    public ArrayList<Integer> dump(String word, int i, Set<State> marked) {
		   
		
		ArrayList<Integer> tabAccept= new ArrayList<Integer>();   
		    
		    String label="";
		    if (word!=null) {
			label=" label=\"";
			for (int j=0; j<=word.length(); j++) {
			    if (j==i)
				label+='>';
			    else
				label+=' ';
			    if (j<word.length())
				label+=word.charAt(j);
			}
			
			label += "\";\n";
		    }
		    
		    // imprimer tout ce qui est sp'ecifique au graphe
		    
		    // afficher les epsilon transitions
			for (State s: accept){
			tabAccept.add(s.id);
		
			}
			
		    
		 
		    // imprimer les autres transitions 
		    
		   return tabAccept;
		
	    }
	    public Automaton FinalAutomatom(String post){
		
		Stackaut pile=new Stackaut(4);
		int i=0;
		while(i<post.length()){
			if(post.charAt(i)<='z'&& post.charAt(i)>='a'){
				Automaton a1=new Automaton();
				a1=new_CHAR(post.charAt(i));
				pile.push(a1);
			}
			else if(post.charAt(i)=='*'){
				Automaton a1=new Automaton();
				a1=pile.pop();
				a1=new_STAR(a1);
				pile.push(a1);
				 
			}
			else if(post.charAt(i)=='|')
			{
				
				Automaton a1=new Automaton();
				a1=pile.pop();
				a1=new_OR(a1,pile.pop());
				pile.push(a1);
			
				
			}
			else if(post.charAt(i)=='.')
			{
				Automaton a1=new Automaton();
				a1=pile.pop();
				a1=new_SEQ(pile.pop(),a1);
				pile.push(a1);
			
			}
			
			i++;
			
			
			
		}
		Automaton a1=new Automaton();
		if (pile.isEmpty()==false)
		a1=pile.pop();
		else a1=new_WILD();
		return a1;
	}
	public ArrayList<Integer> epsilontransition(int id)
	{
		ArrayList<Integer>list=new ArrayList<Integer>();
	list.add(id);
		int j=1;
		for (State s: states) 
		{
		for (State n: s.epsilon) 
		{
		
		   if(s.id==id && n.id!=id){
			list.add(n.id);
			
		}
		}
		}
		
		return list;
		
		
	}
	public ArrayList<Integer> firstlist(){
		ArrayList<Integer> list=epsilontransition(initial.id);
		int k=list.size();
		for(int j=1;j<k;j++)
		{
			ArrayList<Integer> l=epsilontransition(list.get(j));
			for(int i=0 ;i<l.size();i++){
				int v=l.get(i);
			if (list.contains(v)){
				l.remove(i);
			}
			}
			list.addAll(k,l);
			k=list.size();
			
		}
		Collections.sort(list);
		
		return list;
	}
		public ArrayList<Integer> transition_list(char c,ArrayList<Integer> list)
		{
		ArrayList<Integer> lo=new ArrayList<Integer>();
		for(int j=0;j<list.size();j++)
		for (State s: states) {
			if(s.id==list.get(j)){
			    State n2 =s.transition[c];
			if(n2!=null) lo.add(n2.id);
				}
				}
			int	k1=lo.size();
		for(int j=0;j<k1;j++){
		ArrayList<Integer> l1=epsilontransition(lo.get(j));
		for(int i=0 ;i<l1.size();i++){
			int v=l1.get(i);
		if (lo.contains(v)){
			l1.remove(i);
		}
		}
		lo.addAll(k1,l1);
		k1=lo.size();
		}
		for(int j=0;j<lo.size()-1;j++)
		{for(int i=j+1;i<lo.size();i++)
		{
			
			if(lo.get(i)==lo.get(j))lo.remove(i);
		}
			
		}
		
		Collections.sort(lo);
		
		return lo;
		
		
		
	}
	public ArrayList<ArrayList<Character>> inout(ArrayList<ArrayList<Integer>> vecteurAccept,ArrayList<ArrayList<ArrayList<Integer>>> MatTrans,ArrayList<Character> c,ArrayList<ArrayList<Integer>> a)
	{
	ArrayList<ArrayList<Character>> inout=new ArrayList<ArrayList<Character>>();
		for(int i=0;i<vecteurAccept.size();i++)
		{for(int j=0;j<MatTrans.size();j++)
			{
				if(MatTrans.get(j).get(0).equals(vecteurAccept.get(i)))
				
			{
				
			
				ArrayList<Character> unzero= new ArrayList<Character>();
				for(int k=1;k < MatTrans.get(j).size();k++)
				{ for(int l=0;l<a.size();l++)
				{
				      if(MatTrans.get(j).get(k).equals(a.get(l)))unzero.add(c.get(l));
				}
					
				}
				
				ArrayList<Character> unzero1= (ArrayList<Character>)unzero.clone();
				inout.add(unzero1);
				unzero.clear();
				
				
			}
			}
		}
		return inout;
	}
	public void decomposition(ArrayList<ArrayList<ArrayList<Integer>>> groupe,ArrayList<ArrayList<Integer>> vecteurAccept,ArrayList<ArrayList<ArrayList<Integer>>> MatTrans,ArrayList<Character> c,ArrayList<ArrayList<Integer>> a)
	{
		int i=0;
		while(vecteurAccept.isEmpty()==false)
		{
		ArrayList<ArrayList<Character>> inout=new ArrayList<ArrayList<Character>>();
		inout=inout(vecteurAccept,MatTrans,c,a);
		Set hashset=new HashSet(inout);
		ArrayList<Integer> var=new ArrayList<Integer>();
		ArrayList<Character> var2= new ArrayList<Character>();
		if(hashset.size()==1){
			ArrayList<ArrayList<Integer>> var3=(ArrayList<ArrayList<Integer>>)vecteurAccept.clone();
			groupe.add(var3);
			vecteurAccept.clear();
			
			
		}
		else{
			
			var=(ArrayList<Integer>)vecteurAccept.get(0).clone();
			vecteurAccept.remove(0);
			var2=(ArrayList<Character>)inout.get(0).clone();
			inout.remove(0);
			if(inout.contains(var2)==false){ 
				ArrayList<ArrayList<Integer>> varl= new ArrayList<ArrayList<Integer>>();
				varl.add(var);
				groupe.add((ArrayList<ArrayList<Integer>>)varl.clone());
				varl.clear();
				}
			else if(i<vecteurAccept.size()){
				
			i++;
			vecteurAccept.add(var);
			inout.add(var2);
			}
			else
			{
				vecteurAccept.add(var);
				inout.add(var2);
				ArrayList<ArrayList<Integer>> vecteur1= new ArrayList<ArrayList<Integer>>();
				ArrayList<ArrayList<Integer>> vecteur2= new ArrayList<ArrayList<Integer>>();
			    for(int j=0;j<inout.size();j++)
			{
				if(var2.equals(inout.get(j)))vecteur1.add((ArrayList<Integer>)vecteurAccept.get(j).clone());
				else vecteur2.add((ArrayList<Integer>)vecteurAccept.get(j).clone());
			}
				vecteurAccept.clear();
				
				decomposition(groupe, vecteur1, MatTrans,c,a);
				decomposition(groupe, vecteur2, MatTrans,c,a);
			}
		
				}} 
	}
	 public ArrayList<ArrayList<Integer>> SearchTrans(ArrayList<ArrayList<ArrayList<Integer>>> matTrans,ArrayList<Integer> vect){
		
		int i=0;boolean test=false;int res=0;
		while (test==false&&i<matTrans.size()) {
		if(matTrans.get(i).get(0).equals((ArrayList<Integer> )vect)){
			test=true;
			res= i ;
			break;
			}	
		i++;
		}
		
		return matTrans.get(res);
		
		
	}
	public int SearchGroupe(ArrayList<ArrayList<ArrayList<Integer>>> groupe,ArrayList<Integer> vect){
		int i=0;boolean test=false;int res=0;
		while(test==false&&i<groupe.size()){
			if (groupe.get(i).contains((ArrayList<Integer> )vect)) {
				test=false;
				res=i;
			}
			i++;
			
		}
		return res;
	}
	public void dessine(String word, int i, Set<State> marked) {
		try {
			PrintWriter out = new PrintWriter(String.format("epsilon.dot",i));
			String label="";
			if (word!=null) {
				label=" label=\"";
				for (int j=0; j<=word.length(); j++) {
					if (j==i)
						label+='>';
					else
						label+=' ';
					if (j<word.length())
						label+=word.charAt(j);
				}
				label += "\";\n";
			}
			
			// imprimer tout ce qui est sp'ecifique au graphe
			out.println("digraph {\n"+
						" rankdir=LR;\n"+
						label+
						" init [shape=point];\n"+
						" init->"+initial.id+";\n"      );
			
			// imprimer les 'etats
			for (State s: states) {
				out.print(" "+s.id+" [");
				if (accept.contains(s))
					out.print("shape=doublecircle");
				else
					out.print("shape=circle");
				if (marked!=null && marked.contains(s))
					out.print(",style=filled");
				out.println("];");
			}
			
			out.println();
			// imprimer les epsilon transitions
			for (State s: states) 
				for (State n: s.epsilon) 
					out.println(" "+s.id+"->"+n.id+";");
			
			out.println();
			// imprimer les autres transitions 
			for (State s: states) {
				char  c1 = 0;
				State n1 = null;
				for (char c2=0; c2<=256; c2++) {
					State n2 = (c2<256) ? s.transition[c2] : null;
					if (n1!=n2) {
						if (n1!=null) {
							if (c1==0 && c2==256)
								label = "\"?\"";
							else if (c1==c2-1)
								label = "\""+c1+"\"";
							else
								label = "\""+c1+"-"+(c2-1)+"\"";
							out.println(" "+s.id+"->"+n1.id+"[label="+label+"];");
						}
						c1 = c2;
						n1 = n2;
					}
				}
			}
			out.println("}");
			out.close();
		} 
		catch (FileNotFoundException e) {
			throw new Error("fichier tmp??.dot n'a pas pu ^etre cr'ee");
		}
	}
        
	public String dessine_optimal(ArrayList<Integer> accepteur,ArrayList<ArrayList<ArrayList<Integer>>> groupe,ArrayList<ArrayList<ArrayList<Integer>>> MatTrans,String alphabet,int res) {
		try {
			PrintWriter out = new PrintWriter(String.format("optimal.dot",res));
			String s=new String();
			String label="";
			// imprimer tout ce qui est sp'ecifique au graphe
			out.println("digraph {\n"+
						" rankdir=LR;\n"+
						label+
						" init [shape=point];\n"+
						" init->"+res+";\n"      );
						s+="digraph {\n"+
						" rankdir=LR;\n"+
						label+
						" init [shape=point];\n"+
						" init->"+res+";\n";
			
			// imprimer les 'etats
			for (int j=0;j<groupe.size();j++) {
				out.print(" "+j+" [");
				s+=" "+j+" [";
				if (accepteur.contains(j))
				{
					out.print("shape=doublecircle");
					s+="shape=doublecircle";
				}
				else
				{
					out.print("shape=circle");
				    s+="shape=circle";
				}
				out.println("];");
				s+="];";
			}
			
			out.println();
						// imprimer les transitions 
						for(int i=0;i<groupe.size();i++)
						{
ArrayList<ArrayList<Integer>> vectrans=(ArrayList<ArrayList<Integer>>)SearchTrans(MatTrans,groupe.get(i).get(0)).clone();
						for(int j=1;j<vectrans.size();j++)
							{out.println(" "+i+"->"+SearchGroupe(groupe,vectrans.get(j))+"[label="+alphabet.charAt(j-1)+"];");
							s+=" "+i+"->"+SearchGroupe(groupe,vectrans.get(j))+"[label="+alphabet.charAt(j-1)+"];";
							}
						}

			out.println("}");
			s+="}";
			out.close();
			return s;
		} 
		catch (FileNotFoundException e) {
			throw new Error("fichier tmp??.dot n'a pas pu ^etre cr'ee");
		}}
                public void dessine_deteministe(ArrayList<ArrayList<Integer>> accepteur,ArrayList<ArrayList<ArrayList<Integer>>> MatTrans,String alphabet,ArrayList<Character> vecteuralphabet,ArrayList<ArrayList<Integer>> vecteurtrans) {
		try {
			PrintWriter out = new PrintWriter(String.format("deterministe.dot",vecteuralphabet.get(0)));
			String label="";
			// imprimer tout ce qui est sp'ecifique au graphe
			out.println("digraph {\n"+
						" rankdir=LR;\n"+
						label+
						" init [shape=point];\n"+
						" init->"+vecteuralphabet.get(0)+";\n"      );
			
			// imprimer les 'etats
			for (int j=0;j<vecteurtrans.size();j++) {
				out.print(" "+vecteuralphabet.get(j)+" [");
				if (accepteur.contains(vecteurtrans.get(j)))
					out.print("shape=doublecircle");
				else
				
					out.print("shape=circle");
				out.println("];");
			}
			
			out.println();
						// imprimer les transitions 
						for(int i=0;i<MatTrans.size();i++)
						for(int j=1;j<MatTrans.get(i).size();j++)
						{
							out.println(" "+vecteuralphabet.get(vecteurtrans.indexOf(MatTrans.get(i).get(0)))+"->"+vecteuralphabet.get(vecteurtrans.indexOf(MatTrans.get(i).get(j)))+"[label="+alphabet.charAt(j-1)+"];");
						}

			out.println("}");
			out.close();
		} 
		catch (FileNotFoundException e) {
			throw new Error("fichier tmp??.dot n'a pas pu ^etre cr'ee");
		}
	}
                public String dessine_optimal2(ArrayList<Integer> accepteur,ArrayList<ArrayList<ArrayList<Integer>>> groupe,ArrayList<ArrayList<ArrayList<Integer>>> MatTrans,String alphabet,int res) {
	ArrayList<ArrayList<Integer>> opt=new ArrayList<ArrayList<Integer>>();
		try {
			PrintWriter out = new PrintWriter(String.format("optimal2.dot",res));
			String s=new String();
			String label="";
			// imprimer tout ce qui est sp'ecifique au graphe
			out.println("digraph {\n"+
						" rankdir=LR;\n"+
						label+
						" init [shape=point];\n"+
						" init->"+res+";\n"      );
						s+="digraph {\n"+
						" rankdir=LR;\n"+
						label+
						" init [shape=point];\n"+
						" init->"+res+";\n"      ;
			
			// imprimer les 'etats
			for (int j=0;j<groupe.size();j++) {
				out.print(" "+j+" [");
				s+=" "+j+" [";
				if (accepteur.contains(j))
				{
				
				out.print("shape=doublecircle");
				s+="shape=doublecircle";
				}
				else{
					out.print("shape=circle");
				    s+="shape=circle";
				}
				out.println("];");
				s+="];";
			}
			
			out.println();
						// imprimer les transitions 
						for(int i=0;i<groupe.size();i++)
						{
ArrayList<ArrayList<Integer>> vectrans=(ArrayList<ArrayList<Integer>>)SearchTrans(MatTrans,groupe.get(i).get(0)).clone();
						for(int j=1;j<vectrans.size();j++)
							{out.println(" "+i+"->"+SearchGroupe(groupe,vectrans.get(j))+"[label="+alphabet.charAt(j-1)+"];");
							s+=" "+i+"->"+SearchGroupe(groupe,vectrans.get(j))+"[label="+alphabet.charAt(j-1)+"];";
							}
						}

			out.println("}");
			s+="}";
			out.close();
			return s;
		} 
		catch (FileNotFoundException e) {
			throw new Error("fichier tmp??.dot n'a pas pu ^etre cr'ee");
		}}
                public void dessine2(String word, int i, Set<State> marked) {
		try {
			PrintWriter out = new PrintWriter(String.format("graphe.dot",i));
			String label="";
			if (word!=null) {
				label=" label=\"";
				for (int j=0; j<=word.length(); j++) {
					if (j==i)
						label+='>';
					else
						label+=' ';
					if (j<word.length())
						label+=word.charAt(j);
				}
				label += "\";\n";
			}
			
			// imprimer tout ce qui est sp'ecifique au graphe
			out.println("digraph {\n"+
						" rankdir=LR;\n"+
						label+
						" init [shape=point];\n"+
						" init->"+initial.id+";\n"      );
			
			// imprimer les 'etats
			for (State s: states) {
				out.print(" "+s.id+" [");
				if (accept.contains(s))
					out.print("shape=doublecircle");
				else
					out.print("shape=circle");
				if (marked!=null && marked.contains(s))
					out.print(",style=filled");
				out.println("];");
			}
			
			out.println();
			// imprimer les epsilon transitions
			for (State s: states) 
				for (State n: s.epsilon) 
					out.println(" "+s.id+"->"+n.id+";");
			
			out.println();
			// imprimer les autres transitions 
			for (State s: states) {
				char  c1 = 0;
				State n1 = null;
				for (char c2=0; c2<=256; c2++) {
					State n2 = (c2<256) ? s.transition[c2] : null;
					if (n1!=n2) {
						if (n1!=null) {
							if (c1==0 && c2==256)
								label = "\"?\"";
							else if (c1==c2-1)
								label = "\""+c1+"\"";
							else
								label = "\""+c1+"-"+(c2-1)+"\"";
							out.println(" "+s.id+"->"+n1.id+"[label="+label+"];");
						}
						c1 = c2;
						n1 = n2;
					}
				}
			}
			out.println("}");
			out.close();
		} 
		catch (FileNotFoundException e) {
			throw new Error("fichier tmp??.dot n'a pas pu ^etre cr'ee");
		}
	}
                public void dessine_deteministe2(ArrayList<ArrayList<Integer>> accepteur,ArrayList<ArrayList<ArrayList<Integer>>> MatTrans,String alphabet,ArrayList<Character> vecteuralphabet,ArrayList<ArrayList<Integer>> vecteurtrans) {
		try {
			PrintWriter out = new PrintWriter(String.format("deterministe2.dot",vecteuralphabet.get(0)));
			
			String label="";
			// imprimer tout ce qui est sp'ecifique au graphe
			out.println("digraph {\n"+
						" rankdir=LR;\n"+
						label+
						" init [shape=point];\n"+
						" init->"+vecteuralphabet.get(0)+";\n"      );
			
			// imprimer les 'etats
			for (int j=0;j<vecteurtrans.size();j++) {
				out.print(" "+vecteuralphabet.get(j)+" [");
				if (accepteur.contains(vecteurtrans.get(j)))
					out.print("shape=doublecircle");
				else
					out.print("shape=circle");
				out.println("];");
			}
			
			out.println();
						// imprimer les transitions 
						for(int i=0;i<MatTrans.size();i++)
						for(int j=1;j<MatTrans.get(i).size();j++)
						{
							out.println(" "+vecteuralphabet.get(vecteurtrans.indexOf(MatTrans.get(i).get(0)))+"->"+vecteuralphabet.get(vecteurtrans.indexOf(MatTrans.get(i).get(j)))+"[label="+alphabet.charAt(j-1)+"];");
						}

			out.println("}");
			out.close();
		} 
		catch (FileNotFoundException e) {
			throw new Error("fichier tmp??.dot n'a pas pu ^etre cr'ee");
		}
	}
	public boolean testegalite(ArrayList<ArrayList<ArrayList<Integer>>> groupe1,ArrayList<ArrayList<ArrayList<Integer>>> group2,ArrayList<ArrayList<ArrayList<Integer>>> MatTrans1,ArrayList<ArrayList<ArrayList<Integer>>> MatTrans2,String alphabet1,String alphabet2,ArrayList<Integer> accept1,ArrayList<Integer> accept2)
		{
	           String s=new String();
	           
		if(alphabet1.length()!=alphabet2.length())return false;
		else {for(int i=0;i<alphabet1.length();i++)
	            
	        s+=alphabet2.charAt(i);
		if(alphabet1.indexOf(s)==-1)return false;
		if(group2.size()!=groupe1.size())return false;
		}
		for(int i=0;i<group2.size();i++)
								{
		ArrayList<ArrayList<Integer>> vectrans2=(ArrayList<ArrayList<Integer>>)SearchTrans(MatTrans2,group2.get(i).get(0)).clone();
		ArrayList<ArrayList<Integer>> vectrans1=(ArrayList<ArrayList<Integer>>)SearchTrans(MatTrans1,groupe1.get(i).get(0)).clone();
								for(int j=1;j<vectrans2.size();j++)
								if(SearchGroupe(group2,vectrans2.get(j))!=SearchGroupe(groupe1,vectrans1.get(j)))return false;
								}
								if(accept1.size()!=accept2.size())return false;
								else{
								for(int i=0;i<accept1.size();i++)
								{
									if(accept1.contains(accept2.get(i))==false)return false;
								}
								}
								return true;
		
		}
	}


	
	
