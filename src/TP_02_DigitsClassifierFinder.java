import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class TP_02_DigitsClassifierFinder {

    public static Instances[] split(Instances instances, int percent){
        int size1 = (int) Math.round(instances.numInstances() * percent / 100);
        int size2 = instances.numInstances() - size1;
        Instances part1 = new Instances(instances, 0, size1);
        Instances part2 = new Instances(instances, size1, size2);
        return new Instances[] { part1, part2} ;
    }

    public static Classifier findBestClassifier(Instances instances) throws Exception {

        // split
        Instances[] split = split(instances, 70) ;
        Instances train = split[0] ;
        Instances cross = split[1];

        Evaluation eval ;
        double bestKappa  ;
        Classifier bestClassifier ;

        J48 lr = new J48();
        eval = eval(lr, train, cross);
        bestKappa = eval.kappa() ;
        bestClassifier = lr ;

        NaiveBayes bayes = new NaiveBayes() ;
        eval = eval(bayes, train, cross);
        if (eval.kappa() > bestKappa) {
            bestClassifier = bayes ;
            bestKappa = eval.kappa() ;
        }

        LibSVM svm = new LibSVM();
        svm.setGamma(0.1);
        svm.setCost(1);
        eval = eval(svm, train, cross);
        if (eval.kappa() > bestKappa) {
            bestClassifier = svm ;
            bestKappa = eval.kappa() ;
        }

        SMO smo = new SMO() ;
        eval = eval(smo, train, cross);
        if (eval.kappa() > bestKappa) {
            bestClassifier = smo ;
            bestKappa = eval.kappa() ;
        }

        MultilayerPerceptron mlp = new MultilayerPerceptron();
        mlp.setLearningRate(0.01);
        mlp.setMomentum(0.9);
        mlp.setTrainingTime(100);
        mlp.setSeed(1);
        mlp.setHiddenLayers("25") ;
        eval = eval(mlp, train, cross);
        if (eval.kappa() > bestKappa) {
            bestClassifier = mlp ;
            bestKappa = eval.kappa() ;
        }

        return bestClassifier ;
    }

    private static Evaluation eval(Classifier classifier, Instances train, Instances test) throws Exception {

        System.out.println("--- " + classifier.getClass().getSimpleName() + " ---");
        System.out.println();
        System.out.println("Training...");
        classifier.buildClassifier(train);

        Evaluation eval ;
        eval = new Evaluation(test);
        eval.evaluateModel(classifier, test);
        System.out.println(eval.toSummaryString());

        return eval ;
    }

    public static void main(String[] args) throws Exception {

        Instances dataset = TP_00_DigitsLoader.loadDigitsData() ;

        Instances[] split = split(dataset, 80) ;
        dataset = split[0];
        Instances test = split[1] ;

        // test and eval classifiers
        Classifier bestClassifier = findBestClassifier(dataset);
        System.out.println("------------------------------------");
        System.out.println();
        System.out.println("Best classifier found : " + bestClassifier.getClass().getSimpleName());

        // classify an instance
        double result = bestClassifier.classifyInstance(test.firstInstance());
        System.out.println();
        System.out.println("--- Classify an instance ---");
        System.out.println();
        System.out.println("Real value : " + test.classAttribute().value((int)test.firstInstance().classValue())) ;
        System.out.println("Predicted : " + test.classAttribute().value((int)result));

    }

}
