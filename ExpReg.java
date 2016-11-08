import java.util.*;
import java.util.*;
import java.io.*;
public class ExpReg {
	String exp;
	Stack pile;
	
	public ExpReg (String s)
	{
		exp=s;
		pile=new Stack(1000);
	}
	public boolean verif()
	{boolean test;
		try {
			for(int i=0;i<exp.length();i++)
			{
				switch (exp.charAt(i)) {
					case '(':pile.push(new Character('(')); break;
					case ')':pile.pop();break;
				}
			}

		} catch (EmptyStackException e) {
			return false;
		}
		if (pile.isEmpty())test=true;
		else return false;
		if((exp.charAt(0)=='*')||(exp.charAt(0)=='|'))return false;
		else test=true;
		
		for(int i=1;i<exp.length();i++)
		   {
			if ((exp.charAt(i)=='*')||(exp.charAt(i)=='|')||((exp.charAt(i)>='a')&&(exp.charAt(i)<='z'))||(exp.charAt(i)=='(')||(exp.charAt(i)==')'))test=true;
			else return false;
			   
			}
		for(int i=1;i<exp.length();i++)
		{   
			if (exp.charAt(i)=='*') {
      if((exp.charAt(i-1)==')')||((exp.charAt(i-1)>='a')&&(exp.charAt(i-1)<='z'))||(exp.charAt(i-1)=='0')||(exp.charAt(i-1)=='1'))  test=true;
          else return false;
                        }
if (exp.charAt(i)=='|') {
	if((exp.charAt(i-1)=='(')||(exp.charAt(i+1)==')')||(exp.charAt(i-1)=='|')||(exp.charAt(i+1)=='|'))return false;
			else test=true;
										}
			}
			return test;
	
		
	}
	public String post1() {
		String post="";
		int i;
		
		for(i=0;i<this.exp.length();i++){
			
			if(exp.charAt(i)=='*'){
			
			post+="*";
			post+="!";
			}
			else{
				post+=exp.charAt(i);
			}
			
		}
		return post;
	}
	public String post2() {
		String post="";
		int i;
		
		for(i=0;i<this.exp.length()-1;i++){
			if(((((exp.charAt(i)>='a' && exp.charAt(i)<='z')||(exp.charAt(i)>='0' && exp.charAt(i)<='9'))||exp.charAt(i)==')')||exp.charAt(i)=='!')&&(((exp.charAt(i+1)>='a' && exp.charAt(i+1)<='z')||(exp.charAt(i+1)>='0' && exp.charAt(i+1)<='9'))||exp.charAt(i+1)=='('))
			{
				post+=exp.charAt(i);
				post+=".";
			}
			
			
			else{
				post+=exp.charAt(i);
			}
			
		}
		post+=exp.charAt(i++);
		return post;
	}
	public String getalphabet()
	{
		String alphabet =new String();
		ArrayList<Character> alpha=new ArrayList<Character>();
			for(int i=0;i<this.exp.length();i++){
				if((exp.charAt(i)>='a' && exp.charAt(i)<='z'))
				alpha.add(exp.charAt(i));
			
		      }
			for(int j=0;j<alpha.size();j++)
			{
				for(int i=j+1;i<alpha.size();i++)
			    {
				
				if(alpha.get(i)==alpha.get(j))alpha.remove(i);
			     }
				
			}
			for(int i=0;i<alpha.size();i++)
			{
				alphabet+=alpha.get(i);
			}

	return alphabet;
	     
	
	}
		
	
	public static void main(String []args){
		String ch1="(aaaabbbb*c)";
		ExpReg exp=new ExpReg(ch1);
		System.out.println(exp.verif());	
		System.out.println(new ExpReg(exp.post1()).post2());
		
		
		
		System.out.print(exp.getalphabet());
		
		
		
	
	}
	
}