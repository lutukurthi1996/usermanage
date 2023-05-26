package project.usermanage;

public class Password {
	public String passwordEncryption(String password)
	 {
		 char[] p= password.toCharArray();
		 String encryption="";
		 for(int i=0;i <p.length;i++)
		 {
			 encryption+=(char)(p[i]+2);
		 }
		 return encryption;
	 
	 }
	 
	  public boolean passwordConditions(String password)
	{
		  if(password.length()>=8)
		  {
			  String splsymbol="!#$%&'()*+,-./:;<=>?@[]^_`{|}~";
			  String number="0123456789";
			  String lowercase="abcdefghijklmnopqrstuvwxyz";
			  String[] p=password.split("");
			  int splCount=0,numCount=0,lowCount=0,upCount=0;
			  for(int i=0;i<p.length;i++)
			  {
				  if(splsymbol.contains(p[i]))
					  splCount++;
				  else if(number.contains(p[i]))
					  numCount++;
				  else if(lowercase.contains(p[i]))
					  lowCount++;
				  else
					  upCount++;
			  }
			  if(splCount>0&&numCount>0&&lowCount>0&&upCount>0)
			  {
				  return true;
			  }
			  else
			  {
				  return false;
			  }
		  }
		  else
		  {
			  return false;
		  }
		  }
	
}
