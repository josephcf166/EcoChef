package com.example.ecochef

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

enum class Ingredients (var Iname : String, var imageLink: String) {
    Almond("Almond","https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/a/almond_16x9.jpg"),
    Anchovies("Anchovies","https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/a/anchovy_16x9.jpg"),
    Apple("Apple","https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/a/apple_16x9.jpg"),
    Apricot("Apricot","https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/a/apricot_16x9.jpg"),
    Artichoke("Artichoke","https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/a/artichoke_16x9.jpg"),
    Asparagus("Asparagus","https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/a/asparagus_16x9.jpg"),
    Aubergine("Aubergine","https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/a/aubergine_16x9.jpg"),
    Avocado("Avocado","https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/a/avocado_16x9.jpg"),
    Bacon("Bacon","https://images.immediate.co.uk/production/volatile/sites/30/2019/11/Bacon-rashers-in-a-pan-72c07f4.jpg?quality=90&webp=true&resize=700,636"),
    Bagel("Bagel","https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/bagel_16x9.jpg"),
    Baguette("Baguette", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/baguette_16x9.jpg"),
    Banana("Banana", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/banana_16x9.jpg"),
    Basil("Basil", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/basil_16x9.jpg"),
    Beef("Beef", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/beef_mince_16x9.jpg"),
    Beer("Beer", "https://hips.hearstapps.com/del.h-cdn.co/assets/cm/15/11/3200x3272/54f65d39ab05d_-_183341797.jpg?resize=980:*"),
    Biscuits("Biscuits", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/biscuit_16x9.jpg"),
    Blackberry("Blackberry", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/blackberry_16x9.jpg"),
    Blackcurrant("Blackcurrant", "https://images.immediate.co.uk/production/volatile/sites/30/2020/02/Blackcurrants-a51d0ff.jpg?quality=90&webp=true&resize=1200,1090"),
    Blueberry("Blueberry", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/blueberry_16x9.jpg"),
    Bread("Bread", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/bread_16x9.jpg"),
    Brie("Brie", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/brie_cheese_16x9.jpg"),
    Brioche("Brioche", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/brioche_16x9.jpg"),
    Broccoli("Broccoli", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/broccoli_16x9.jpg"),
    Burger("Burger", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/b/beef_burger_16x9.jpg"),
    Butter("Butter", "https://www.southernliving.com/thmb/e9PRDV-qQ9F1GRYh4C_SBAi4foI=/750x0/filters:no_upscale():max_bytes(150000):strip_icc()/How_To_Soften_Butter_013-2000-61e8b4e1ad9c431887472483ae714dbb.jpg"),
    Cabbage("Cabbage", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/cabbage_16x9.jpg"),
    Caramel("Caramel", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/caramel_16x9.jpg"),
    Carrot("Carrot", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/carrot_16x9.jpg"),
    Cauliflower("Cauliflower", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/cauliflower_16x9.jpg"),
    Celery("Celery", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/celery_16x9.jpg"),
    Cheddar("Cheddar", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/cheddar_cheese_16x9.jpg"),
    Cherry("Cherry", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/cherry_16x9.jpg"),
    Chicken("Chicken", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/chicken_16x9.jpg"),
    Chilli("Chilli", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/chilli_16x9.jpg"),
    Chocolate("Chocolate", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/chocolate_16x9.jpg"),
    Chorizo("Chorizo", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/chorizo_16x9.jpg"),
    Cinnamon("Cinnamon", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/cinnamon_16x9.jpg"),
    CocoaPowder("Cocoa Powder", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/cocoa_16x9.jpg"),
    Cod("Cod", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/cod_16x9.jpg"),
    Coffee("Coffee", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/coffee_beans_16x9.jpg"),
    Crab("Crab", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/crab_16x9.jpg"),
    Cream("Cream", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/cream_16x9.jpg"),
    Cucumber("Cucumber", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/cucumber_16x9.jpg"),
    Duck("Duck", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/d/duck_16x9.jpg"),
    Egg("Egg", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/e/egg_16x9.jpg"),
    Fish("Fish", "https://ichef.bbci.co.uk/food/ic/food_16x9_832/recipes/fennelandherbbarbecu_67598_16x9.jpg"),
    Flour("Flour", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/f/flour_16x9.jpg"),
    Gammon("Gammon", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/g/gammon_16x9.jpg"),
    Garlic("Garlic", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/g/garlic_16x9.jpg"),
    Gelatine("Gelatine", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/g/gelatine_16x9.jpg"),
    Ginger("Ginger", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/g/ginger_16x9.jpg"),
    Gnocchi("Gnocchi", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/g/gnocchi_16x9.jpg"),
    Gravy("Gravy", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/g/gravy_16x9.jpg"),
    Haddock("Haddock", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/h/haddock_16x9.jpg"),
    Halloumi("Halloumi", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/h/halloumi_cheese_16x9.jpg"),
    Ham("Ham", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/h/ham_16x9.jpg"),
    Hazelnut("Hazelnut", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/h/hazelnut_16x9.jpg"),
    Honey("Honey", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/h/honey_16x9.jpg"),
    Hummus("Hummus", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/h/hummus_16x9.jpg"),
    Kiwi("Kiwi", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/k/kiwi_fruit_16x9.jpg"),
    Lamb("lamb", "https://www.laboiteny.com/cdn/shop/articles/RedRubbedBabyLambChopsPg101.jpg?v=1615995080"),
    Lardons("Lardons", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/l/lardons_16x9.jpg"),
    Lasagne("Lasagne", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/l/lasagne_sheets_16x9.jpg"),
    Lemon("Lemon", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/l/lemon_16x9.jpg"),
    Lettuce("Lettuce", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/l/lettuce_16x9.jpg"),
    Lobster("Lobster", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/l/lobster_16x9.jpg"),
    Macaroni("Macaroni", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/m/macaroni_16x9.jpg"),
    Mango("Mango", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/m/mango_16x9.jpg"),
    Milk("Milk", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/m/milk_16x9.jpg"),
    Mozzarella("Mozzarella", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/m/mozzarella_cheese_16x9.jpg"),
    Mushroom("Mushroom", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/m/mushroom_16x9.jpg"),
    Noodles("Noodles", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/n/noodle_16x9.jpg"),
    Oats("Oats", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/o/oats_16x9.jpg"),
    Onion("Onion", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/o/onion_16x9.jpg"),
    Orange("Orange", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/o/orange_16x9.jpg"),
    Pasta("Pasta", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/p/pasta_16x9.jpg"),
    Peach("Peach", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/p/peach_16x9.jpg"),
    Pepper("Pepper", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/p/pepper_16x9.jpg"),
    Pesto("Pesto", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/p/pesto_16x9.jpg"),
    Pork("Pork", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/p/pork_chop_16x9.jpg"),
    Potato("Potato", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/p/potato_16x9.jpg"),
    Sugar("Sugar", "https://ichef.bbci.co.uk/food/ic/food_16x9_608/foods/c/caster_sugar_16x9.jpg")
}

fun getIngredientsList(): MutableList<Ingredients> {
    val ingredientsList = mutableStateListOf<Ingredients>()
    for (ingredient in Ingredients.values()) {
        ingredientsList.add(ingredient)
    }
    return ingredientsList
}




