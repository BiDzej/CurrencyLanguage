import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class VariablesAndFunctions {
    //hashMap of hashmaps on every deepness LvL
    //first string in a hidden map is a variable name
    //second string in a hidden map is a variable type
    private HashMap<Integer, HashMap<String, String>> variables;

    //hashmap containing function name and parameters in a queue
    private HashMap<String, ArrayList<String>> functions;

    //list containing currencies types
    private ArrayList<String> currenciesTypes;

    public VariablesAndFunctions()
    {
        variables = new HashMap<>();
        functions = new HashMap<>();
        currenciesTypes = new ArrayList<>();

        //embedded currencies
        currenciesTypes.add("PLN");
        currenciesTypes.add("EUR");
        currenciesTypes.add("CHF");
        currenciesTypes.add("USD");
        currenciesTypes.add("GBP");
        currenciesTypes.add("JPY");
    }

    //returns type or null if does not exist
    public String getVariableType(String name, Integer deepness)
    {
        //System.out.println("\n\nChecking : " + name + "\n\n");
        for(int i = deepness; i >= 0; --i)
        {
            HashMap<String, String> tmp = variables.get(i);
            if(tmp!=null) {
                String tmp1 = tmp.get(name);
                if(tmp1!=null)
                    return tmp1;
            }
        }
        return null;
    }

    public void addVariable(String name, String type, Integer deepness)
    {
        HashMap<String, String> tmp = variables.get(deepness);
        if(tmp==null)
            throw new NullPointerException("HashMap with this deepness does not exist.");
        tmp.put(name, type);
    }

    public void createVariablesLevel(Integer deepness) throws Exception {
        if(variables.get(deepness)!=null)
            throw new Exception("Variables HashMap on this level already exists.");
        variables.put(deepness, new HashMap<String, String>());
    }

    public void deleteVariablesLevel(Integer deepness)
    {
        if(variables.get(deepness)!=null)
            variables.remove(deepness);
    }

    public boolean functionWithParamsExist(String name, ArrayList<String> params)
    {
        return functions.get(name).equals(params);

    }

    public boolean functionExist(String name)
    {
        if(functions.containsKey(name))
            return true;
        return false;
    }

    public void addFunction(String name, ArrayList<String> params)
    {
        functions.put(name, new ArrayList<>(params));
    }

    public boolean doesCurrencyTypeExist(String name)
    {
        return currenciesTypes.contains(name);
    }

    public void addCurrencyType(String name)
    {
        currenciesTypes.add(name);
    }
}
