# README for Pathpedia

Pathpedia is an automated system to parse immunohistochemistry(IHC) reports using a grammar-based approach and parse the corresponding surgical pathology(SP) reports using a tree-based approach.


# Citing Pathpedia

Lee et. al  "Automated Extraction of Biomarker Information from Pathology Reports", In preparation, 2018. 


# Requirement

* [Python] (https://www.python.org) (3.5.0). The easiest way to install Python and all of the necessary dependencies is to download and install [Anaconda] 
* [Java] JDK 1.8.*. You can download JDK file (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html ) The following links are instruction for installing.. (https://docs.oracle.com/javase/8/docs/technotes/guides/install/linux_jdk.html#BJFJJEFG )

# USAGE

## Data
You must have a same format of pathology report to run as ours unless modify the source code for the parsing ( we described which source code are needed to be change). For more detail, please see our paper for the Pathpedia.
```
[IHC report]
Histopathologic Diagnosis: S12-00001 (1),(5),(6)

IHC Result:
   - Smooth muscle actin: Positive
   - p63: No loss
   - Cytokeratin 5/6: Positive
```

```
[SP report]
MICRO (2 HE and 14 Fro)

DIAGNOSIS:
A) Pituitary gland, transsphenoid approach and tumor removal:
     Pituitary adenoma with
     1) pleomorphic nuclei
     2) atrophic normal pituitary tissue

B) Dura, excision:
    Fibrous tissue with
     no diagnostic abnormalities
```

## IHC parser (kr.ac.knu.ml.exec.extractor.PADocExtractor.java)

### Arguments
The following are arguments for train.py, the training script:
* '-i inputfile'. Excel file which is formatted as example file(IHC_sample.xlsx) in the sample folder. Multiple IHC reports can be loaded with one excel file. *IHC report ID should start with "PA".
* '-p pid'. If you want run only one report among the reports within a excel file, you can specify report ID with this pid option.
* '-o outputfile'. Name of the outputfile

### Examples
For simplicity, example shows an example of usage with jar file.
```
$ java -jar PADocExtractor.jar -i IHCsample.xlsx -o IHCsample_out.xlsx
```
If you want to specify a report to run,
```
$ java -jar PADocExtractor.jar -i IHCsample.xlsx -p PA1200001 -o IHCsample_out.xlsx
```

## SP parser (kr.ac.knu.ml.exec.extractor.SDocExtractor.java)

### Arguments
The following are arguments for train.py, the training script:
* '-i inputfile'. Excel file which is formatted as example file(SP_sample.xlsx) in the sample folder. Multiple SP reports can be loaded with one excel file. *SP report ID should start with "S".
* '-p pid'. If you want run only one report among the reports within a excel file, you can specify report ID with this pid option.
* '-o outputfile'. Name of the outputfile

### Examples
For simplicity, example shows an example of usage with jar file.
```
$ java -jar SDocExtractor.jar -i SPsample.xlsx -o SPsample_out.xlsx
```
If you want to specify a report to run,
```
$ java -jar SDocExtractor.jar -i SPsample.xlsx -p S1200001 -o SPsample_out.xlsx
```

## Localization

### IHC parser
If you want to change formatting determinants used in IHC reports, check java code named "kr.ac.knu.ml.parser.ImmunityDocumentParser.java" We annotated detailed information which variables are needed to be within the code.

### SP parser
If you want to change formatting determinants used in SP reports, check java code named "kr.ac.knu.ml.parser. SurgeonDocumentParser.java" We annotated detailed information which variables are needed to be changed within the code.
