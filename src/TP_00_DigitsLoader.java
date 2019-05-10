import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.instance.Randomize;

public class TP_00_DigitsLoader {

    public static Instances loadDigitsData() throws Exception {
        // load data from csv file
        CSVLoader loader = new CSVLoader();
        loader.setSource(TP_02_DigitsClassifierFinder.class.getResourceAsStream("resources/digits.csv"));
        Instances dataset = loader.getDataSet();

        // identifiy class attribute
        dataset.setClassIndex(dataset.attribute("y").index());

        // convert class attribute from numeric to label
        NumericToNominal convert = new NumericToNominal() ;
        convert.setInputFormat(dataset) ;
        String[] options= new String[2];
        options[0]="-R";
        options[1]= ""+ (dataset.classIndex()+1) ;  //range of variables to make numeric
        convert.setOptions(options);
        dataset = Filter.useFilter(dataset, convert) ;

        // shuffle data
        Randomize randomize = new Randomize() ;
        randomize.setInputFormat(dataset) ;
        dataset = Filter.useFilter(dataset, randomize) ;

        return dataset ;
    }

}
