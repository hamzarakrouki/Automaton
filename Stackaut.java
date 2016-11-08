public class Stackaut {
		private int maxSize;
		private Automaton[] stackArray;
		private int top;
		public Stackaut(int max) {
		   maxSize = max;
		   stackArray = new Automaton[maxSize];
		   top = -1;
		}
		public void push(Automaton j) {
		   stackArray[++top] = j;
		}
		public Automaton pop() {
		   return stackArray[top--];
		}
		public Automaton peek() {
		   return stackArray[top];
		}
		public boolean isEmpty() {
		   return (top == -1);
	   }
	  
	
}