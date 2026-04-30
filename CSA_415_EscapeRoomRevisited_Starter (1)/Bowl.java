import java.util.ArrayList;
public class Bowl{
  private String[][] recipes;
  private ArrayList<String> ingredients;
  
  public Bowl(){
    //add recipes here. Format: name is first, then ingredients.
    recipes = new String[][]{{"Vanilla cake batter,butter,egg,egg,egg,flour,milk,sugar"},
              {"Chocolate cake batter,butter,egg,egg,egg,flour,milk,sugar,cocoa"},
              {"Vanilla icing,butter,milk,sugar"},
              {"Chocolate icing,butter,cocoa,milk,sugar"}};
  }
  
  //Adds one ingredient to the bowl
  public void addIngredient(String item){
    ingredients.add(item);
  }
  
  //Adds multiple ingredients to the bowl
  public void setIngredients(String[] items){
    for(String i:items)ingredients.add(i);
  }
  
  //clears the bowl of all ingredients
  public void clearBowl(){
    ingredients = new ArrayList<String>();
  }
  
  //returns an arraylist of all ingredients
  public ArrayList<String> getIngredients(){
    return ingredients;
  }
  
  //checks if ingredients match a recipe, and if so turns them into the product.
  public void mix(){
    sort();
    String[] temp = findRecipe().split("",1);
    if(temp[0]!=null){
      clearBowl();
      setIngredients(temp);
    }
  }
  
  //sorts the ingredients for easier comparison in mix()
  private void sort(){//selection sort
    int n = ingredients.size();
    for (int i = 0; i < n - 1; i++) {
      int min = i;
      for (int j = i + 1; j < n; j++) {
        if (ingredients.get(j).compareTo(ingredients.get(min))<0) {
          min = j;
        }
      }
      String temp = ingredients.get(i);
      ingredients.set(i,ingredients.get(min));
      ingredients.set(min,temp);
    }
  }
  
  // recipes has to be pre-sorted
  //compares the ingredients in the bowl with a list of recipes.
  private String findRecipe(){
    boolean found = false;
    for(int i = 0;i<recipes.length&&!found;i++){
      for(int j = 1;j<ingredients.size();j++){
        if(!ingredients.get(j).equals(recipes[i][j+1]))break;
        if(j==ingredients.size()-1)return recipes[i][0];
      }
    }
    return null;
  }
}
