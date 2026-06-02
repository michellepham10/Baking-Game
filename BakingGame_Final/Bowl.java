import java.util.ArrayList;
import java.util.Arrays;
public class Bowl{
  private String[][] recipes;
  private ArrayList<String> ingredients;
  
  public Bowl(){
    //add recipes here. Format: name is first, then ingredients.
    recipes = new String[][]{{"vanilla cake batter","butter","eggs","flour","milk","sugar"},
              {"strawberry cake batter","butter","eggs","flour","milk","strawberries","sugar"},
              {"chocolate cake batter","butter","chocolate","eggs","flour","milk","sugar"},
              {"chocolate cupcake batter","butter","chocolate","eggs","flour","milk","sourcream","sugar"},
              {"cookie batter","butter","chocolate","eggs","flour","sugar"}};
              
    ingredients = new ArrayList<String>();
  }
  
  //Adds multiple ingredients to the bowl
  public void addIngredient(ArrayList<String> items){
    ingredients.addAll(items);
    sort();
  }
  public void addIngredient(String item){
    ingredients.add(item);
    sort();
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
    String temp = findRecipe();
    System.out.println(ingredients+","+temp);
    if(temp!=null){
      clearBowl();
      ingredients.addAll(Arrays.asList(temp));
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
    for(int i = 0;i<recipes.length;i++){
      for(int j = 0;j<ingredients.size()&&j<recipes[i].length-1;j++){
        if(ingredients.size()!=recipes[i].length-1)break;
        if(!ingredients.get(j).equals(recipes[i][j+1])){
          break;
        }
        if(j==ingredients.size()-1){
          return recipes[i][0];
        }
      }
    }
    return null;
  }
  public int findRecipeIdx(String str){
    for(int row = 0;row<recipes.length;row++){
      if(recipes[row][0].equals(str))return row;
    }
    return -1;
  }
}
