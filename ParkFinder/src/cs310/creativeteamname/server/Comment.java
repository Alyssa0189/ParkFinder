package cs310.creativeteamname.server;


	import javax.jdo.annotations.IdGeneratorStrategy;
	import javax.jdo.annotations.IdentityType;
	import javax.jdo.annotations.PersistenceCapable;
	import javax.jdo.annotations.Persistent;
	import javax.jdo.annotations.PrimaryKey;


	@PersistenceCapable(identityType = IdentityType.APPLICATION)
	public class Comment {
		
		@PrimaryKey
		@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
		private String input;
		
		public Comment(String input) {
			this.input=input;
			}
		
		public String getInput() {
			return this.input;
		}
		
		public void setInput (String input){
			this.input=input;
		}
		
		
	}



