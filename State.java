// cours 421b - c.durr - 2006

import java.util.*;
import java.io.*;

/** State realise les etats des automates.
 */
class State implements Comparable {
	static int next_id=0;
	
	int id; // l'identificateur (unique) de l'etat
	
	// table de transitions. Idealement on aimerait utiliser un dictionaire,
	// mais peut-^etre c'est trop d'introduire l'utilisation de HashMap
	// aux 'etudiants `a ce niveau
	State   transition[];

	/* Contrairement a l'enonce on appele ce type Set<State> et non
	 * TreeSet<State> pour pouvoir changer un jour en HashSet<State>
	 * si on veut et n'avoir qu'une ligne `a changer
	 *
	 * epsilon contient tous les 'etats vers lequel il y a une epsilon trans.
	 */
	Set<State>  epsilon;
	
	State() {
		id = next_id++;
		transition  = new State[256];
		epsilon     = new TreeSet<State>();
	}
	State(int j)
	{
		id=j;
		transition  = new State[256];
		epsilon     = new TreeSet<State>();
		
	}
	
	
	// ajoute une transition pour la lettre c
	void addTransition(char c, State next) {
		// on v'erifie qu'aucune transition n'est encore d'efinie pour c
		assert transition[c]==null; 
		transition[c] = next;
	}

	// ajoute une epsilon transition
	void addTransition(State next) {
	epsilon.add(next);
	}
	
	public boolean equals(Object o){
	if(!(o instanceof State))
	    return false;
	State s = (State)o;
	return id==s.id;
	}
	
	public int compareTo(Object o) {
	State s = (State)o;
	return id-s.id;
	}

	static void epsilon_closure(Set<State> set) {
	TreeSet<State> toAdd = new TreeSet<State>();
	do {
	    toAdd = new TreeSet<State>();
	    for (State state: set) 
		for (State next: state.epsilon)
		    if (!set.contains(next)) {
			toAdd.add(next);
		    }
			set.addAll(toAdd);
	} while (!toAdd.isEmpty());
	}

}
