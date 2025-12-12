import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.defuzzifier.*;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionPieceWiseLinear;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTrapetzoidal;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import net.sourceforge.jFuzzyLogic.ruleAccumulationMethod.RuleAccumulationMethodMax;
import net.sourceforge.jFuzzyLogic.ruleActivationMethod.RuleActivationMethodMin;
import java.util.HashMap;
import net.sourceforge.jFuzzyLogic.plot.*;


public class FIS2 {

    public static FIS distance() {
        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("distance", functionBlock);

        Variable distance = new Variable("distance");
        Variable matching = new Variable("matching");

        functionBlock.setVariable(distance.getName(), distance);
        functionBlock.setVariable(matching.getName(), matching);

        Value lowDistanceX[] = {new Value(0), new Value(100)};
        Value lowDistanceY[] = {new Value(1), new Value(0)};
        MembershipFunction lowDistance = new MembershipFunctionPieceWiseLinear(lowDistanceX, lowDistanceY);
        LinguisticTerm ltLowDistance = new LinguisticTerm("lowDistance", lowDistance);
        distance.add(ltLowDistance);

        Value matchX[] = {new Value(0), new Value(1)};
        Value matchY[] = {new Value(0), new Value(1)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        matching.add(ltMatch);

        matching.setDefuzzifier(new DefuzzifierLeftMostMax(matching));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());

        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(distance, "lowDistance", false);
        rule1.addConsequent(matching, "match", false);
        ruleBlock.add(rule1);


        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS d_ss() {
        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("level one + distance", functionBlock);

        Variable fis1 = new Variable("fis1");
        Variable distance = new Variable("distance");
        Variable result = new Variable("result");

        functionBlock.setVariable(distance.getName(), distance);
        functionBlock.setVariable(fis1.getName(), fis1);
        functionBlock.setVariable(result.getName(), result);

        //fuzzify distance
        Value nearX[] = {new Value(2), new Value(50)};
        Value nearY[] = {new Value(1), new Value(0)};
        MembershipFunction near = new MembershipFunctionPieceWiseLinear(nearX, nearY);

        Value farX[] = {new Value(2), new Value(50)};
        Value farY[] = {new Value(0), new Value(1)};
        MembershipFunction far = new MembershipFunctionPieceWiseLinear(farX, farY);

        LinguisticTerm ltNear = new LinguisticTerm("near", near);
        LinguisticTerm ltFar = new LinguisticTerm("far", far);
        distance.add(ltNear);
        distance.add(ltFar);

        //fuzzify result
        Value veryNotMatchX[] = { new Value(-0.298), new Value(0), new Value(0.3)};
        Value veryNotMatchY[] = { new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryNotMatch = new MembershipFunctionPieceWiseLinear(veryNotMatchX, veryNotMatchY);

        Value notMatchX[] = {new Value(0.1), new Value(0.3), new Value(0.5)};
        Value notMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction notMatch = new MembershipFunctionPieceWiseLinear(notMatchX, notMatchY);

        Value partlyMatchX[] = {new Value(0.3), new Value(0.5), new Value(0.7)};
        Value partlyMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction partlyMatch = new MembershipFunctionPieceWiseLinear(partlyMatchX, partlyMatchY);

        Value matchX[] = {new Value(0.5), new Value(0.7), new Value(0.9)};
        Value matchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);

        Value veryMatchX[] = {new Value(0.7), new Value(1), new Value(1.298)};
        Value veryMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryMatch = new MembershipFunctionPieceWiseLinear(veryMatchX, veryMatchY);

        LinguisticTerm ltVeryNotMatch = new LinguisticTerm("veryNotMatch", veryNotMatch);
        LinguisticTerm ltNotMatch = new LinguisticTerm("notMatch", notMatch);
        LinguisticTerm ltPartlyMatch = new LinguisticTerm("partlyMatch", partlyMatch);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        LinguisticTerm ltVeryMatch = new LinguisticTerm("veryMatch", veryMatch);

        fis1.add(ltVeryNotMatch);
        fis1.add(ltNotMatch);
        fis1.add(ltPartlyMatch);
        fis1.add(ltMatch);
        fis1.add(ltVeryMatch);

        result.add(ltVeryNotMatch);
        result.add(ltNotMatch);
        result.add(ltPartlyMatch);
        result.add(ltMatch);
        result.add(ltVeryMatch);

        result.setDefuzzifier(new DefuzzifierCenterOfArea(result));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());

        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(distance, "near", false);
        rule1.addAntecedent(fis1, "veryMatch", false);
        rule1.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        rule2.addAntecedent(fis1, "veryMatch", false);
        rule2.addAntecedent(distance, "far", false);
        rule2.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        rule3.addAntecedent(fis1, "match", false);
        rule3.addAntecedent(distance, "near", false);
        rule3.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule3);

        Rule rule4 = new Rule("Rule4", ruleBlock);
        rule4.addAntecedent(fis1, "match", false);
        rule4.addAntecedent(distance, "far", false);
        rule4.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule4);

        Rule rule5 = new Rule("Rule5", ruleBlock);
        rule5.addAntecedent(fis1, "partlyMatch", false);
        rule5.addAntecedent(distance, "near", false);
        rule5.addConsequent(result, "match", false);
        ruleBlock.add(rule5);

        Rule rule6 = new Rule("Rule6", ruleBlock);
        rule6.addAntecedent(fis1, "partlyMatch", false);
        rule6.addAntecedent(distance, "far", false);
        rule6.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule6);

        Rule rule7 = new Rule("Rule7", ruleBlock);
        rule7.addAntecedent(fis1, "notMatch", false);
        rule7.addAntecedent(distance, "near", false);
        rule7.addConsequent(result, "match", false);
        ruleBlock.add(rule7);

        Rule rule8 = new Rule("Rule8", ruleBlock);
        rule8.addAntecedent(fis1, "notMatch", false);
        rule8.addAntecedent(distance, "far", false);
        rule8.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule8);

        Rule rule9 = new Rule("Rule9", ruleBlock);
        rule9.addAntecedent(fis1, "veryNotMatch", false);
        rule9.addAntecedent(distance, "near", false);
        rule9.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule9);

        Rule rule10 = new Rule("Rule10", ruleBlock);
        rule10.addAntecedent(fis1, "veryNotMatch", false);
        rule10.addAntecedent(distance, "far", false);
        rule10.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule10);

        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS s_s() {
        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("speed + slope", functionBlock);

        Variable speed = new Variable("speed");
        Variable slope = new Variable("slope");
        Variable result = new Variable("result");

        functionBlock.setVariable(slope.getName(), slope);
        functionBlock.setVariable(speed.getName(), speed);
        functionBlock.setVariable(result.getName(), result);

        // fuzzify slope
        Value lowX[] = {new Value(0), new Value(15)};
        Value lowY[] = {new Value(1), new Value(0)};
        MembershipFunction low = new MembershipFunctionPieceWiseLinear(lowX, lowY);
        Value mediumX[] = {new Value(0), new Value(15), new Value(30)};
        Value mediumY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction medium = new MembershipFunctionPieceWiseLinear(mediumX, mediumY);
        Value highX[] = {new Value(15), new Value(30)};
        Value highY[] = {new Value(0), new Value(1)};
        MembershipFunction high = new MembershipFunctionPieceWiseLinear(highX, highY);
        LinguisticTerm ltLow = new LinguisticTerm("low", low);
        LinguisticTerm ltMedium = new LinguisticTerm("medium", medium);
        LinguisticTerm ltHigh = new LinguisticTerm("high", high);
        slope.add(ltLow);
        slope.add(ltMedium);
        slope.add(ltHigh);

        // fuzzify speed
        Value verySlowX[] = {new Value(0.05), new Value(0.15)};
        Value verySlowY[] = {new Value(1), new Value(0)};
        MembershipFunction verySlow = new MembershipFunctionPieceWiseLinear(verySlowX, verySlowY);
        Value slowX[] = {new Value(0.05), new Value(0.15), new Value(0.25)};
        Value slowY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction slow = new MembershipFunctionPieceWiseLinear(slowX, slowY);
        Value fastX[] = {new Value(0.15), new Value(0.25), new Value(0.35)};
        Value fastY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction fast = new MembershipFunctionPieceWiseLinear(fastX, fastY);
        Value veryFastX[] = {new Value(0.25), new Value(0.35)};
        Value veryFastY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryFast = new MembershipFunctionPieceWiseLinear(veryFastX, veryFastY);
        LinguisticTerm ltVerySlow = new LinguisticTerm("verySlow", verySlow);
        LinguisticTerm ltSlow = new LinguisticTerm("slow", slow);
        LinguisticTerm ltFast = new LinguisticTerm("fast", fast);
        LinguisticTerm ltVeryFast = new LinguisticTerm("veryFast", veryFast);
        speed.add(ltVerySlow);
        speed.add(ltSlow);
        speed.add(ltFast);
        speed.add(ltVeryFast);

        //fuzzify result
        Value veryNotMatchX[] = {new Value(-0.298), new Value(0), new Value(0.3)};
        Value veryNotMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryNotMatch = new MembershipFunctionPieceWiseLinear(veryNotMatchX, veryNotMatchY);

        Value notMatchX[] = {new Value(0.1), new Value(0.3), new Value(0.5)};
        Value notMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction notMatch = new MembershipFunctionPieceWiseLinear(notMatchX, notMatchY);

        Value partlyMatchX[] = {new Value(0.3), new Value(0.5), new Value(0.7)};
        Value partlyMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction partlyMatch = new MembershipFunctionPieceWiseLinear(partlyMatchX, partlyMatchY);

        Value matchX[] = {new Value(0.5), new Value(0.7), new Value(0.9)};
        Value matchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);

        Value veryMatchX[] = {new Value(0.7), new Value(1), new Value(1.298)};
        Value veryMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryMatch = new MembershipFunctionPieceWiseLinear(veryMatchX, veryMatchY);

        LinguisticTerm ltVeryNotMatch = new LinguisticTerm("veryNotMatch", veryNotMatch);
        LinguisticTerm ltNotMatch = new LinguisticTerm("notMatch", notMatch);
        LinguisticTerm ltPartlyMatch = new LinguisticTerm("partlyMatch", partlyMatch);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        LinguisticTerm ltVeryMatch = new LinguisticTerm("veryMatch", veryMatch);

        result.add(ltVeryNotMatch);
        result.add(ltNotMatch);
        result.add(ltPartlyMatch);
        result.add(ltMatch);
        result.add(ltVeryMatch);

        result.setDefuzzifier(new DefuzzifierCenterOfArea(result));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());

        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(speed, "verySlow", false);
        rule1.addAntecedent(slope, "low", false);
        rule1.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        rule2.addAntecedent(speed, "verySlow", false);
        rule2.addAntecedent(slope, "medium", false);
        rule2.addConsequent(result, "match", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        rule3.addAntecedent(speed, "verySlow", false);
        rule3.addAntecedent(slope, "high", false);
        rule3.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule3);

        Rule rule4 = new Rule("Rule4", ruleBlock);
        rule4.addAntecedent(speed, "slow", false);
        rule4.addAntecedent(slope, "low", false);
        rule4.addConsequent(result, "match", false);
        ruleBlock.add(rule4);

        Rule rule5 = new Rule("Rule5", ruleBlock);
        rule5.addAntecedent(speed, "slow", false);
        rule5.addAntecedent(slope, "medium", false);
        rule5.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule5);

        Rule rule6 = new Rule("Rule6", ruleBlock);
        rule6.addAntecedent(speed, "slow", false);
        rule6.addAntecedent(slope, "high", false);
        rule6.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule6);

        Rule rule7 = new Rule("Rule7", ruleBlock);
        rule7.addAntecedent(speed, "fast", false);
        rule7.addAntecedent(slope, "low", false);
        rule7.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule7);

        Rule rule8 = new Rule("Rule8", ruleBlock);
        rule8.addAntecedent(speed, "fast", false);
        rule8.addAntecedent(slope, "medium", false);
        rule8.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule8);

        Rule rule9 = new Rule("Rule9", ruleBlock);
        rule9.addAntecedent(speed, "fast", false);
        rule9.addAntecedent(slope, "high", false);
        rule9.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule9);

        Rule rule10 = new Rule("Rule10", ruleBlock);
        rule10.addAntecedent(speed, "veryFast", false);
        rule10.addAntecedent(slope, "low", false);
        rule10.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule10);

        Rule rule11 = new Rule("Rule11", ruleBlock);
        rule11.addAntecedent(speed, "veryFast", false);
        rule11.addAntecedent(slope, "medium", false);
        rule11.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule11);

        Rule rule12 = new Rule("Rule12", ruleBlock);
        rule12.addAntecedent(speed, "veryFast", false);
        rule12.addAntecedent(slope, "high", false);
        rule12.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule12);

        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS d_r() {

        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("difficulity + rating", functionBlock);

        Variable difficulity = new Variable("difficulity");
        Variable rating = new Variable("rating");
        Variable result = new Variable("result");

        functionBlock.setVariable(difficulity.getName(), difficulity);
        functionBlock.setVariable(rating.getName(), rating);
        functionBlock.setVariable(result.getName(), result);

        //fuzzifying difficulity
        Value zeroX[] = {new Value(-1), new Value(0), new Value(1)};
        Value zeroY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction zero = new MembershipFunctionPieceWiseLinear(zeroX, zeroY);

        Value oneX[] = {new Value(0), new Value(1), new Value(2)};
        Value oneY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction one = new MembershipFunctionPieceWiseLinear(oneX, oneY);

        Value twoX[] = {new Value(1), new Value(2), new Value(3)};
        Value twoY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction two = new MembershipFunctionPieceWiseLinear(twoX, twoY);


        LinguisticTerm ltZero = new LinguisticTerm("zero", zero);
        LinguisticTerm ltOne = new LinguisticTerm("one", one);
        LinguisticTerm ltTwo = new LinguisticTerm("two", two);

        difficulity.add(ltZero);
        difficulity.add(ltOne);
        difficulity.add(ltTwo);

        //fuzzify rating
        Value rzeroX[] = {new Value(-1), new Value(0), new Value(1)};
        Value rzeroY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction rzero = new MembershipFunctionPieceWiseLinear(rzeroX, rzeroY);

        Value roneX[] = {new Value(0), new Value(1), new Value(2)};
        Value roneY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction rone = new MembershipFunctionPieceWiseLinear(roneX, roneY);

        Value rtwoX[] = {new Value(1), new Value(2), new Value(3)};
        Value rtwoY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction rtwo = new MembershipFunctionPieceWiseLinear(rtwoX, rtwoY);

        Value rthreeX[] = {new Value(2), new Value(3), new Value(4)};
        Value rthreeY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction rthree = new MembershipFunctionPieceWiseLinear(rthreeX, rthreeY);

        Value rfourX[] = {new Value(3), new Value(4), new Value(5)};
        Value rfourY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction rfour = new MembershipFunctionPieceWiseLinear(rfourX, rfourY);

        LinguisticTerm ltRZero = new LinguisticTerm("rzero", rzero);
        LinguisticTerm ltROne = new LinguisticTerm("rone", rone);
        LinguisticTerm ltRTwo = new LinguisticTerm("rtwo", rtwo);
        LinguisticTerm ltRThree = new LinguisticTerm("rthree", rthree);
        LinguisticTerm ltRFour = new LinguisticTerm("rfour", rfour);

        rating.add(ltRZero);
        rating.add(ltROne);
        rating.add(ltRTwo);
        rating.add(ltRThree);
        rating.add(ltRFour);

        //fuzzify result
        Value veryNotMatchX[] = {new Value(-0.298), new Value(0), new Value(0.3)};
        Value veryNotMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryNotMatch = new MembershipFunctionPieceWiseLinear(veryNotMatchX, veryNotMatchY);

        Value notMatchX[] = {new Value(0.1), new Value(0.3), new Value(0.5)};
        Value notMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction notMatch = new MembershipFunctionPieceWiseLinear(notMatchX, notMatchY);

        Value partlyMatchX[] = {new Value(0.3), new Value(0.5), new Value(0.7)};
        Value partlyMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction partlyMatch = new MembershipFunctionPieceWiseLinear(partlyMatchX, partlyMatchY);

        Value matchX[] = {new Value(0.5), new Value(0.7), new Value(0.9)};
        Value matchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);

        Value veryMatchX[] = {new Value(0.7), new Value(1), new Value(1.298)};
        Value veryMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryMatch = new MembershipFunctionPieceWiseLinear(veryMatchX, veryMatchY);

        LinguisticTerm ltVeryNotMatch = new LinguisticTerm("veryNotMatch", veryNotMatch);
        LinguisticTerm ltNotMatch = new LinguisticTerm("notMatch", notMatch);
        LinguisticTerm ltPartlyMatch = new LinguisticTerm("partlyMatch", partlyMatch);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        LinguisticTerm ltVeryMatch = new LinguisticTerm("veryMatch", veryMatch);

        result.add(ltVeryNotMatch);
        result.add(ltNotMatch);
        result.add(ltPartlyMatch);
        result.add(ltMatch);
        result.add(ltVeryMatch);

        result.setDefuzzifier(new DefuzzifierCenterOfArea(result));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());


        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(rating, "rzero", false);
        rule1.addAntecedent(difficulity, "zero", false);
        rule1.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        rule2.addAntecedent(rating, "rzero", false);
        rule2.addAntecedent(difficulity, "one", false);
        rule2.addConsequent(result, "match", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        rule3.addAntecedent(rating, "rzero", false);
        rule3.addAntecedent(difficulity, "two", false);
        rule3.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule3);

        Rule rule4 = new Rule("Rule4", ruleBlock);
        rule4.addAntecedent(rating, "rone", false);
        rule4.addAntecedent(difficulity, "zero", false);
        rule4.addConsequent(result, "match", false);
        ruleBlock.add(rule4);

        Rule rule5 = new Rule("Rule5", ruleBlock);
        rule5.addAntecedent(rating, "rone", false);
        rule5.addAntecedent(difficulity, "one", false);
        rule5.addConsequent(result, "match", false);
        ruleBlock.add(rule5);

        Rule rule6 = new Rule("Rule6", ruleBlock);
        rule6.addAntecedent(rating, "rone", false);
        rule6.addAntecedent(difficulity, "two", false);
        rule6.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule6);

        Rule rule7 = new Rule("Rule7", ruleBlock);
        rule7.addAntecedent(rating, "rtwo", false);
        rule7.addAntecedent(difficulity, "zero", false);
        rule7.addConsequent(result, "match", false);
        ruleBlock.add(rule7);

        Rule rule8 = new Rule("Rule8", ruleBlock);
        rule8.addAntecedent(rating, "rtwo", false);
        rule8.addAntecedent(difficulity, "one", false);
        rule8.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule8);

        Rule rule9 = new Rule("Rule9", ruleBlock);
        rule9.addAntecedent(rating, "rtwo", false);
        rule9.addAntecedent(difficulity, "two", false);
        rule9.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule9);

        Rule rule10 = new Rule("Rule10", ruleBlock);
        rule10.addAntecedent(rating, "rthree", false);
        rule10.addAntecedent(difficulity, "zero", false);
        rule10.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule10);

        Rule rule11 = new Rule("Rule11", ruleBlock);
        rule11.addAntecedent(rating, "rthree", false);
        rule11.addAntecedent(difficulity, "one", false);
        rule11.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule11);

        Rule rule12 = new Rule("Rule12", ruleBlock);
        rule12.addAntecedent(rating, "rthree", false);
        rule12.addAntecedent(difficulity, "two", false);
        rule12.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule12);

        Rule rule13 = new Rule("Rule13", ruleBlock);
        rule13.addAntecedent(rating, "rfour", false);
        rule13.addAntecedent(difficulity, "zero", false);
        rule13.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule13);

        Rule rule14 = new Rule("Rule14", ruleBlock);
        rule14.addAntecedent(rating, "rfour", false);
        rule14.addAntecedent(difficulity, "one", false);
        rule14.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule14);

        Rule rule15 = new Rule("Rule15", ruleBlock);
        rule15.addAntecedent(rating, "rfour", false);
        rule15.addAntecedent(difficulity, "two", false);
        rule15.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule15);


        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS t_dr() {

        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("time + difficulity + rating", functionBlock);

        Variable fis1 = new Variable("fis1");
        Variable time = new Variable("time");
        Variable result = new Variable("result");

        functionBlock.setVariable(fis1.getName(), fis1);
        functionBlock.setVariable(time.getName(), time);
        functionBlock.setVariable(result.getName(), result);

        //fuzzify time
        Value veryLowX[] = {new Value(1800), new Value(5400)};
        Value veryLowY[] = {new Value(1), new Value(0)};
        MembershipFunction veryLow = new MembershipFunctionPieceWiseLinear(veryLowX, veryLowY);

        Value lowX[] = {new Value(1800), new Value(5400), new Value(9000)};
        Value lowY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction low = new MembershipFunctionPieceWiseLinear(lowX, lowY);

        Value mediumX[] = {new Value(5400), new Value(9000), new Value(12600)};
        Value mediumY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction medium = new MembershipFunctionPieceWiseLinear(mediumX, mediumY);

        Value highX[] = {new Value(9000), new Value(12600), new Value(16200)};
        Value highY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction high = new MembershipFunctionPieceWiseLinear(highX, highY);

        Value veryHighX[] = {new Value(12600), new Value(16200)};
        Value veryHighY[] = {new Value(0), new Value(1)};
        MembershipFunction veryHigh = new MembershipFunctionPieceWiseLinear(veryHighX, veryHighY);

        LinguisticTerm ltVeryLow = new LinguisticTerm("veryLow", veryLow);
        LinguisticTerm ltLow = new LinguisticTerm("low", low);
        LinguisticTerm ltMedium = new LinguisticTerm("medium", medium);
        LinguisticTerm ltHigh = new LinguisticTerm("high", high);
        LinguisticTerm ltVeryHigh = new LinguisticTerm("veryHigh", veryHigh);

        time.add(ltVeryLow);
        time.add(ltLow);
        time.add(ltMedium);
        time.add(ltHigh);
        time.add(ltVeryHigh);

        //fuzzify result
        Value veryNotMatchX[] = {new Value(-0.298), new Value(0), new Value(0.3)};
        Value veryNotMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryNotMatch = new MembershipFunctionPieceWiseLinear(veryNotMatchX, veryNotMatchY);

        Value notMatchX[] = {new Value(0.1), new Value(0.3), new Value(0.5)};
        Value notMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction notMatch = new MembershipFunctionPieceWiseLinear(notMatchX, notMatchY);

        Value partlyMatchX[] = {new Value(0.3), new Value(0.5), new Value(0.7)};
        Value partlyMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction partlyMatch = new MembershipFunctionPieceWiseLinear(partlyMatchX, partlyMatchY);

        Value matchX[] = {new Value(0.5), new Value(0.7), new Value(0.9)};
        Value matchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);

        Value veryMatchX[] = {new Value(0.7), new Value(1), new Value(1.298)};
        Value veryMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryMatch = new MembershipFunctionPieceWiseLinear(veryMatchX, veryMatchY);

        LinguisticTerm ltVeryNotMatch = new LinguisticTerm("veryNotMatch", veryNotMatch);
        LinguisticTerm ltNotMatch = new LinguisticTerm("notMatch", notMatch);
        LinguisticTerm ltPartlyMatch = new LinguisticTerm("partlyMatch", partlyMatch);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        LinguisticTerm ltVeryMatch = new LinguisticTerm("veryMatch", veryMatch);

        fis1.add(ltVeryNotMatch);
        fis1.add(ltNotMatch);
        fis1.add(ltPartlyMatch);
        fis1.add(ltMatch);
        fis1.add(ltVeryMatch);

        result.add(ltVeryNotMatch);
        result.add(ltNotMatch);
        result.add(ltPartlyMatch);
        result.add(ltMatch);
        result.add(ltVeryMatch);

        result.setDefuzzifier(new DefuzzifierCenterOfArea(result));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());


        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(fis1, "veryMatch", false);
        rule1.addAntecedent(time, "veryLow", false);
        rule1.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        rule2.addAntecedent(fis1, "veryMatch", false);
        rule2.addAntecedent(time, "low", false);
        rule2.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        rule3.addAntecedent(fis1, "veryMatch", false);
        rule3.addAntecedent(time, "medium", false);
        rule3.addConsequent(result, "match", false);
        ruleBlock.add(rule3);

        Rule rule4 = new Rule("Rule4", ruleBlock);
        rule4.addAntecedent(fis1, "veryMatch", false);
        rule4.addAntecedent(time, "high", false);
        rule4.addConsequent(result, "match", false);
        ruleBlock.add(rule4);

        Rule rule5 = new Rule("Rule5", ruleBlock);
        rule5.addAntecedent(fis1, "veryMatch", false);
        rule5.addAntecedent(time, "veryHigh", false);
        rule5.addConsequent(result, "match", false);
        ruleBlock.add(rule5);

        Rule rule6 = new Rule("Rule6", ruleBlock);
        rule6.addAntecedent(fis1, "match", false);
        rule6.addAntecedent(time, "veryLow", false);
        rule6.addConsequent(result, "match", false);
        ruleBlock.add(rule6);

        Rule rule7 = new Rule("Rule7", ruleBlock);
        rule7.addAntecedent(fis1, "match", false);
        rule7.addAntecedent(time, "low", false);
        rule7.addConsequent(result, "match", false);
        ruleBlock.add(rule7);

        Rule rule8 = new Rule("Rule8", ruleBlock);
        rule8.addAntecedent(fis1, "match", false);
        rule8.addAntecedent(time, "medium", false);
        rule8.addConsequent(result, "match", false);
        ruleBlock.add(rule8);

        Rule rule9 = new Rule("Rule9", ruleBlock);
        rule9.addAntecedent(fis1, "match", false);
        rule9.addAntecedent(time, "high", false);
        rule9.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule9);

        Rule rule10 = new Rule("Rule10", ruleBlock);
        rule10.addAntecedent(fis1, "match", false);
        rule10.addAntecedent(time, "veryHigh", false);
        rule10.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule10);

        Rule rule11 = new Rule("Rule11", ruleBlock);
        rule11.addAntecedent(fis1, "partlyMatch", false);
        rule11.addAntecedent(time, "veryLow", false);
        rule11.addConsequent(result, "match", false);
        ruleBlock.add(rule11);

        Rule rule12 = new Rule("Rule12", ruleBlock);
        rule12.addAntecedent(fis1, "partlyMatch", false);
        rule12.addAntecedent(time, "low", false);
        rule12.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule12);

        Rule rule13 = new Rule("Rule13", ruleBlock);
        rule13.addAntecedent(fis1, "partlyMatch", false);
        rule13.addAntecedent(time, "medium", false);
        rule13.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule13);

        Rule rule14 = new Rule("Rule14", ruleBlock);
        rule14.addAntecedent(fis1, "partlyMatch", false);
        rule14.addAntecedent(time, "high", false);
        rule14.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule14);

        Rule rule15 = new Rule("Rule15", ruleBlock);
        rule15.addAntecedent(fis1, "partlyMatch", false);
        rule15.addAntecedent(time, "veryHigh", false);
        rule15.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule15);

        Rule rule16 = new Rule("Rule16", ruleBlock);
        rule16.addAntecedent(fis1, "notMatch", false);
        rule16.addAntecedent(time, "veryLow", false);
        rule16.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule16);

        Rule rule17 = new Rule("Rule17", ruleBlock);
        rule17.addAntecedent(fis1, "notMatch", false);
        rule17.addAntecedent(time, "low", false);
        rule17.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule17);

        Rule rule18 = new Rule("Rule18", ruleBlock);
        rule18.addAntecedent(fis1, "notMatch", false);
        rule18.addAntecedent(time, "medium", false);
        rule18.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule18);

        Rule rule19 = new Rule("Rule19", ruleBlock);
        rule19.addAntecedent(fis1, "notMatch", false);
        rule19.addAntecedent(time, "high", false);
        rule19.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule19);

        Rule rule20 = new Rule("Rule20", ruleBlock);
        rule20.addAntecedent(fis1, "notMatch", false);
        rule20.addAntecedent(time, "veryHigh", false);
        rule20.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule20);

        Rule rule21 = new Rule("Rule21", ruleBlock);
        rule21.addAntecedent(fis1, "veryNotMatch", false);
        rule21.addAntecedent(time, "veryLow", false);
        rule21.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule21);

        Rule rule22 = new Rule("Rule22", ruleBlock);
        rule22.addAntecedent(fis1, "veryNotMatch", false);
        rule22.addAntecedent(time, "low", false);
        rule22.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule22);

        Rule rule23 = new Rule("Rule23", ruleBlock);
        rule23.addAntecedent(fis1, "veryNotMatch", false);
        rule23.addAntecedent(time, "medium", false);
        rule23.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule23);

        Rule rule24 = new Rule("Rule24", ruleBlock);
        rule24.addAntecedent(fis1, "veryNotMatch", false);
        rule24.addAntecedent(time, "high", false);
        rule24.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule24);

        Rule rule25 = new Rule("Rule25", ruleBlock);
        rule25.addAntecedent(fis1, "veryNotMatch", false);
        rule25.addAntecedent(time, "veryHigh", false);
        rule25.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule25);

        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS tdr_d() {

        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("time + difficulity + rating + distance", functionBlock);

        Variable fis1 = new Variable("fis1"); //tdr
        Variable fis2 = new Variable("fis2"); //distance
        Variable result = new Variable("result");

        functionBlock.setVariable(fis1.getName(), fis1);
        functionBlock.setVariable(fis2.getName(), fis2);
        functionBlock.setVariable(result.getName(), result);

        //fuzzify result
        Value veryNotMatchX[] = {new Value(-0.298), new Value(0), new Value(0.3)};
        Value veryNotMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryNotMatch = new MembershipFunctionPieceWiseLinear(veryNotMatchX, veryNotMatchY);

        Value notMatchX[] = {new Value(0.1), new Value(0.3), new Value(0.5)};
        Value notMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction notMatch = new MembershipFunctionPieceWiseLinear(notMatchX, notMatchY);

        Value partlyMatchX[] = {new Value(0.3), new Value(0.5), new Value(0.7)};
        Value partlyMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction partlyMatch = new MembershipFunctionPieceWiseLinear(partlyMatchX, partlyMatchY);

        Value matchX[] = {new Value(0.5), new Value(0.7), new Value(0.9)};
        Value matchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);

        Value veryMatchX[] = {new Value(0.7), new Value(1), new Value(1.298)};
        Value veryMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryMatch = new MembershipFunctionPieceWiseLinear(veryMatchX, veryMatchY);

        LinguisticTerm ltVeryNotMatch = new LinguisticTerm("veryNotMatch", veryNotMatch);
        LinguisticTerm ltNotMatch = new LinguisticTerm("notMatch", notMatch);
        LinguisticTerm ltPartlyMatch = new LinguisticTerm("partlyMatch", partlyMatch);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        LinguisticTerm ltVeryMatch = new LinguisticTerm("veryMatch", veryMatch);

        fis1.add(ltVeryNotMatch);
        fis1.add(ltNotMatch);
        fis1.add(ltPartlyMatch);
        fis1.add(ltMatch);
        fis1.add(ltVeryMatch);

        fis2.add(ltVeryNotMatch);
        fis2.add(ltNotMatch);
        fis2.add(ltPartlyMatch);
        fis2.add(ltMatch);
        fis2.add(ltVeryMatch);

        result.add(ltVeryNotMatch);
        result.add(ltNotMatch);
        result.add(ltPartlyMatch);
        result.add(ltMatch);
        result.add(ltVeryMatch);

        result.setDefuzzifier(new DefuzzifierCenterOfArea(result));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());

        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(fis1, "veryMatch", false);
        rule1.addAntecedent(fis2, "veryMatch", false);
        rule1.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        rule2.addAntecedent(fis1, "veryMatch", false);
        rule2.addAntecedent(fis2, "match", false);
        rule2.addConsequent(result, "match", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        rule3.addAntecedent(fis1, "veryMatch", false);
        rule3.addAntecedent(fis2, "partlyMatch", false);
        rule3.addConsequent(result, "match", false);
        ruleBlock.add(rule3);

        Rule rule4 = new Rule("Rule4", ruleBlock);
        rule4.addAntecedent(fis1, "veryMatch", false);
        rule4.addAntecedent(fis2, "notMatch", false);
        rule4.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule4);

        Rule rule5 = new Rule("Rule5", ruleBlock);
        rule5.addAntecedent(fis1, "veryMatch", false);
        rule5.addAntecedent(fis2, "veryNotMatch", false);
        rule5.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule5);

        Rule rule6 = new Rule("Rule6", ruleBlock);
        rule6.addAntecedent(fis1, "match", false);
        rule6.addAntecedent(fis2, "veryMatch", false);
        rule6.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule6);

        Rule rule7 = new Rule("Rule7", ruleBlock);
        rule7.addAntecedent(fis1, "match", false);
        rule7.addAntecedent(fis2, "match", false);
        rule7.addConsequent(result, "match", false);
        ruleBlock.add(rule7);

        Rule rule8 = new Rule("Rule8", ruleBlock);
        rule8.addAntecedent(fis1, "match", false);
        rule8.addAntecedent(fis2, "partlyMatch", false);
        rule8.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule8);

        Rule rule9 = new Rule("Rule9", ruleBlock);
        rule9.addAntecedent(fis1, "match", false);
        rule9.addAntecedent(fis2, "notMatch", false);
        rule9.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule9);

        Rule rule10 = new Rule("Rule10", ruleBlock);
        rule10.addAntecedent(fis1, "match", false);
        rule10.addAntecedent(fis2, "veryNotMatch", false);
        rule10.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule10);

        Rule rule11 = new Rule("Rule11", ruleBlock);
        rule11.addAntecedent(fis1, "partlyMatch", false);
        rule11.addAntecedent(fis2, "veryMatch", false);
        rule11.addConsequent(result, "match", false);
        ruleBlock.add(rule11);

        Rule rule12 = new Rule("Rule12", ruleBlock);
        rule12.addAntecedent(fis1, "partlyMatch", false);
        rule12.addAntecedent(fis2, "match", false);
        rule12.addConsequent(result, "match", false);
        ruleBlock.add(rule12);

        Rule rule13 = new Rule("Rule13", ruleBlock);
        rule13.addAntecedent(fis1, "partlyMatch", false);
        rule13.addAntecedent(fis2, "partlyMatch", false);
        rule13.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule13);

        Rule rule14 = new Rule("Rule14", ruleBlock);
        rule14.addAntecedent(fis1, "partlyMatch", false);
        rule14.addAntecedent(fis2, "notMatch", false);
        rule14.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule14);

        Rule rule15 = new Rule("Rule15", ruleBlock);
        rule15.addAntecedent(fis1, "partlyMatch", false);
        rule15.addAntecedent(fis2, "veryNotMatch", false);
        rule15.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule15);

        Rule rule16 = new Rule("Rule16", ruleBlock);
        rule16.addAntecedent(fis1, "notMatch", false);
        rule16.addAntecedent(fis2, "veryMatch", false);
        rule16.addConsequent(result, "match", false);
        ruleBlock.add(rule16);

        Rule rule17 = new Rule("Rule17", ruleBlock);
        rule17.addAntecedent(fis1, "notMatch", false);
        rule17.addAntecedent(fis2, "match", false);
        rule17.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule17);

        Rule rule18 = new Rule("Rule18", ruleBlock);
        rule18.addAntecedent(fis1, "notMatch", false);
        rule18.addAntecedent(fis2, "partlyMatch", false);
        rule18.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule18);

        Rule rule19 = new Rule("Rule19", ruleBlock);
        rule19.addAntecedent(fis1, "notMatch", false);
        rule19.addAntecedent(fis2, "notMatch", false);
        rule19.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule19);

        Rule rule20 = new Rule("Rule20", ruleBlock);
        rule20.addAntecedent(fis1, "notMatch", false);
        rule20.addAntecedent(fis2, "veryNotMatch", false);
        rule20.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule20);

        Rule rule21 = new Rule("Rule21", ruleBlock);
        rule21.addAntecedent(fis1, "veryNotMatch", false);
        rule21.addAntecedent(fis2, "veryMatch", false);
        rule21.addConsequent(result, "match", false);
        ruleBlock.add(rule21);

        Rule rule22 = new Rule("Rule22", ruleBlock);
        rule22.addAntecedent(fis1, "veryNotMatch", false);
        rule22.addAntecedent(fis2, "match", false);
        rule22.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule22);

        Rule rule23 = new Rule("Rule23", ruleBlock);
        rule23.addAntecedent(fis1, "veryNotMatch", false);
        rule23.addAntecedent(fis2, "partlyMatch", false);
        rule23.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule23);

        Rule rule24 = new Rule("Rule24", ruleBlock);
        rule24.addAntecedent(fis1, "veryNotMatch", false);
        rule24.addAntecedent(fis2, "notMatch", false);
        rule24.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule24);

        Rule rule25 = new Rule("Rule25", ruleBlock);
        rule25.addAntecedent(fis1, "veryNotMatch", false);
        rule25.addAntecedent(fis2, "veryNotMatch", false);
        rule25.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule25);

        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS ss_tdr() {

        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("time + difficulity + rating", functionBlock);

        Variable fis1 = new Variable("fis1"); //ss
        Variable fis2 = new Variable("fis2"); //tdr
        Variable result = new Variable("result");

        functionBlock.setVariable(fis1.getName(), fis1);
        functionBlock.setVariable(fis2.getName(), fis2);
        functionBlock.setVariable(result.getName(), result);

        //define membership functions and linguistic variables
        Value veryNotMatchX[] = {new Value(-0.298), new Value(0), new Value(0.3)};
        Value veryNotMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryNotMatch = new MembershipFunctionPieceWiseLinear(veryNotMatchX, veryNotMatchY);

        Value notMatchX[] = {new Value(0.1), new Value(0.3), new Value(0.5)};
        Value notMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction notMatch = new MembershipFunctionPieceWiseLinear(notMatchX, notMatchY);

        Value partlyMatchX[] = {new Value(0.3), new Value(0.5), new Value(0.7)};
        Value partlyMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction partlyMatch = new MembershipFunctionPieceWiseLinear(partlyMatchX, partlyMatchY);

        Value matchX[] = {new Value(0.5), new Value(0.7), new Value(0.9)};
        Value matchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);

        Value veryMatchX[] = {new Value(0.7), new Value(1), new Value(1.298)};
        Value veryMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryMatch = new MembershipFunctionPieceWiseLinear(veryMatchX, veryMatchY);

        LinguisticTerm ltVeryNotMatch = new LinguisticTerm("veryNotMatch", veryNotMatch);
        LinguisticTerm ltNotMatch = new LinguisticTerm("notMatch", notMatch);
        LinguisticTerm ltPartlyMatch = new LinguisticTerm("partlyMatch", partlyMatch);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        LinguisticTerm ltVeryMatch = new LinguisticTerm("veryMatch", veryMatch);

        fis1.add(ltVeryNotMatch);
        fis1.add(ltNotMatch);
        fis1.add(ltPartlyMatch);
        fis1.add(ltMatch);
        fis1.add(ltVeryMatch);

        fis2.add(ltVeryNotMatch);
        fis2.add(ltNotMatch);
        fis2.add(ltPartlyMatch);
        fis2.add(ltMatch);
        fis2.add(ltVeryMatch);

        result.add(ltVeryNotMatch);
        result.add(ltNotMatch);
        result.add(ltPartlyMatch);
        result.add(ltMatch);
        result.add(ltVeryMatch);

        result.setDefuzzifier(new DefuzzifierCenterOfArea(result));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());

        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(fis1, "veryMatch", false);
        rule1.addAntecedent(fis2, "veryMatch", false);
        rule1.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        rule2.addAntecedent(fis1, "veryMatch", false);
        rule2.addAntecedent(fis2, "match", false);
        rule2.addConsequent(result, "match", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        rule3.addAntecedent(fis1, "veryMatch", false);
        rule3.addAntecedent(fis2, "partlyMatch", false);
        rule3.addConsequent(result, "match", false);
        ruleBlock.add(rule3);

        Rule rule4 = new Rule("Rule4", ruleBlock);
        rule4.addAntecedent(fis1, "veryMatch", false);
        rule4.addAntecedent(fis2, "notMatch", false);
        rule4.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule4);

        Rule rule5 = new Rule("Rule5", ruleBlock);
        rule5.addAntecedent(fis1, "veryMatch", false);
        rule5.addAntecedent(fis2, "veryNotMatch", false);
        rule5.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule5);

        Rule rule6 = new Rule("Rule6", ruleBlock);
        rule6.addAntecedent(fis1, "match", false);
        rule6.addAntecedent(fis2, "veryMatch", false);
        rule6.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule6);

        Rule rule7 = new Rule("Rule7", ruleBlock);
        rule7.addAntecedent(fis1, "match", false);
        rule7.addAntecedent(fis2, "match", false);
        rule7.addConsequent(result, "match", false);
        ruleBlock.add(rule7);

        Rule rule8 = new Rule("Rule8", ruleBlock);
        rule8.addAntecedent(fis1, "match", false);
        rule8.addAntecedent(fis2, "partlyMatch", false);
        rule8.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule8);

        Rule rule9 = new Rule("Rule9", ruleBlock);
        rule9.addAntecedent(fis1, "match", false);
        rule9.addAntecedent(fis2, "notMatch", false);
        rule9.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule9);

        Rule rule10 = new Rule("Rule10", ruleBlock);
        rule10.addAntecedent(fis1, "match", false);
        rule10.addAntecedent(fis2, "veryNotMatch", false);
        rule10.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule10);

        Rule rule11 = new Rule("Rule11", ruleBlock);
        rule11.addAntecedent(fis1, "partlyMatch", false);
        rule11.addAntecedent(fis2, "veryMatch", false);
        rule11.addConsequent(result, "match", false);
        ruleBlock.add(rule11);

        Rule rule12 = new Rule("Rule12", ruleBlock);
        rule12.addAntecedent(fis1, "partlyMatch", false);
        rule12.addAntecedent(fis2, "match", false);
        rule12.addConsequent(result, "match", false);
        ruleBlock.add(rule12);

        Rule rule13 = new Rule("Rule13", ruleBlock);
        rule13.addAntecedent(fis1, "partlyMatch", false);
        rule13.addAntecedent(fis2, "partlyMatch", false);
        rule13.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule13);

        Rule rule14 = new Rule("Rule14", ruleBlock);
        rule14.addAntecedent(fis1, "partlyMatch", false);
        rule14.addAntecedent(fis2, "notMatch", false);
        rule14.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule14);

        Rule rule15 = new Rule("Rule15", ruleBlock);
        rule15.addAntecedent(fis1, "partlyMatch", false);
        rule15.addAntecedent(fis2, "veryNotMatch", false);
        rule15.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule15);

        Rule rule16 = new Rule("Rule16", ruleBlock);
        rule16.addAntecedent(fis1, "notMatch", false);
        rule16.addAntecedent(fis2, "veryMatch", false);
        rule16.addConsequent(result, "match", false);
        ruleBlock.add(rule16);

        Rule rule17 = new Rule("Rule17", ruleBlock);
        rule17.addAntecedent(fis1, "notMatch", false);
        rule17.addAntecedent(fis2, "match", false);
        rule17.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule17);

        Rule rule18 = new Rule("Rule18", ruleBlock);
        rule18.addAntecedent(fis1, "notMatch", false);
        rule18.addAntecedent(fis2, "partlyMatch", false);
        rule18.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule18);

        Rule rule19 = new Rule("Rule19", ruleBlock);
        rule19.addAntecedent(fis1, "notMatch", false);
        rule19.addAntecedent(fis2, "notMatch", false);
        rule19.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule19);

        Rule rule20 = new Rule("Rule20", ruleBlock);
        rule20.addAntecedent(fis1, "notMatch", false);
        rule20.addAntecedent(fis2, "veryNotMatch", false);
        rule20.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule20);

        Rule rule21 = new Rule("Rule21", ruleBlock);
        rule21.addAntecedent(fis1, "veryNotMatch", false);
        rule21.addAntecedent(fis2, "veryMatch", false);
        rule21.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule21);

        Rule rule22 = new Rule("Rule22", ruleBlock);
        rule22.addAntecedent(fis1, "veryNotMatch", false);
        rule22.addAntecedent(fis2, "match", false);
        rule22.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule22);

        Rule rule23 = new Rule("Rule23", ruleBlock);
        rule23.addAntecedent(fis1, "veryNotMatch", false);
        rule23.addAntecedent(fis2, "partlyMatch", false);
        rule23.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule23);

        Rule rule24 = new Rule("Rule24", ruleBlock);
        rule24.addAntecedent(fis1, "veryNotMatch", false);
        rule24.addAntecedent(fis2, "notMatch", false);
        rule24.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule24);

        Rule rule25 = new Rule("Rule25", ruleBlock);
        rule25.addAntecedent(fis1, "veryNotMatch", false);
        rule25.addAntecedent(fis2, "veryNotMatch", false);
        rule25.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule25);

        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS ssd_tdr() {

        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("time + difficulity + rating", functionBlock);

        Variable fis1 = new Variable("fis1"); // ssd
        Variable fis2 = new Variable("fis2"); // tdr
        Variable result = new Variable("result");

        functionBlock.setVariable(fis1.getName(), fis1);
        functionBlock.setVariable(fis2.getName(), fis2);
        functionBlock.setVariable(result.getName(), result);

        ////define membership functions and linguistic variables
        Value veryNotMatchX[] = {new Value(-0.298), new Value(0), new Value(0.3)};
        Value veryNotMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryNotMatch = new MembershipFunctionPieceWiseLinear(veryNotMatchX, veryNotMatchY);

        Value notMatchX[] = {new Value(0.1), new Value(0.3), new Value(0.5)};
        Value notMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction notMatch = new MembershipFunctionPieceWiseLinear(notMatchX, notMatchY);

        Value partlyMatchX[] = {new Value(0.3), new Value(0.5), new Value(0.7)};
        Value partlyMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction partlyMatch = new MembershipFunctionPieceWiseLinear(partlyMatchX, partlyMatchY);

        Value matchX[] = {new Value(0.5), new Value(0.7), new Value(0.9)};
        Value matchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);

        Value veryMatchX[] = {new Value(0.7), new Value(1), new Value(1.298)};
        Value veryMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryMatch = new MembershipFunctionPieceWiseLinear(veryMatchX, veryMatchY);

        LinguisticTerm ltVeryNotMatch = new LinguisticTerm("veryNotMatch", veryNotMatch);
        LinguisticTerm ltNotMatch = new LinguisticTerm("notMatch", notMatch);
        LinguisticTerm ltPartlyMatch = new LinguisticTerm("partlyMatch", partlyMatch);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        LinguisticTerm ltVeryMatch = new LinguisticTerm("veryMatch", veryMatch);

        fis1.add(ltVeryNotMatch);
        fis1.add(ltNotMatch);
        fis1.add(ltPartlyMatch);
        fis1.add(ltMatch);
        fis1.add(ltVeryMatch);

        fis2.add(ltVeryNotMatch);
        fis2.add(ltNotMatch);
        fis2.add(ltPartlyMatch);
        fis2.add(ltMatch);
        fis2.add(ltVeryMatch);

        result.add(ltVeryNotMatch);
        result.add(ltNotMatch);
        result.add(ltPartlyMatch);
        result.add(ltMatch);
        result.add(ltVeryMatch);

        result.setDefuzzifier(new DefuzzifierCenterOfArea(result));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());

        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(fis1, "veryMatch", false);
        rule1.addAntecedent(fis2, "veryMatch", false);
        rule1.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        rule2.addAntecedent(fis1, "veryMatch", false);
        rule2.addAntecedent(fis2, "match", false);
        rule2.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        rule3.addAntecedent(fis1, "veryMatch", false);
        rule3.addAntecedent(fis2, "partlyMatch", false);
        rule3.addConsequent(result, "match", false);
        ruleBlock.add(rule3);

        Rule rule4 = new Rule("Rule4", ruleBlock);
        rule4.addAntecedent(fis1, "veryMatch", false);
        rule4.addAntecedent(fis2, "notMatch", false);
        rule4.addConsequent(result, "match", false);
        ruleBlock.add(rule4);

        Rule rule5 = new Rule("Rule5", ruleBlock);
        rule5.addAntecedent(fis1, "veryMatch", false);
        rule5.addAntecedent(fis2, "veryNotMatch", false);
        rule5.addConsequent(result, "match", false);
        ruleBlock.add(rule5);

        Rule rule6 = new Rule("Rule6", ruleBlock);
        rule6.addAntecedent(fis1, "match", false);
        rule6.addAntecedent(fis2, "veryMatch", false);
        rule6.addConsequent(result, "match", false);
        ruleBlock.add(rule6);

        Rule rule7 = new Rule("Rule7", ruleBlock);
        rule7.addAntecedent(fis1, "match", false);
        rule7.addAntecedent(fis2, "match", false);
        rule7.addConsequent(result, "match", false);
        ruleBlock.add(rule7);

        Rule rule8 = new Rule("Rule8", ruleBlock);
        rule8.addAntecedent(fis1, "match", false);
        rule8.addAntecedent(fis2, "partlyMatch", false);
        rule8.addConsequent(result, "match", false);
        ruleBlock.add(rule8);

        Rule rule9 = new Rule("Rule9", ruleBlock);
        rule9.addAntecedent(fis1, "match", false);
        rule9.addAntecedent(fis2, "notMatch", false);
        rule9.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule9);

        Rule rule10 = new Rule("Rule10", ruleBlock);
        rule10.addAntecedent(fis1, "match", false);
        rule10.addAntecedent(fis2, "veryNotMatch", false);
        rule10.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule10);

        Rule rule11 = new Rule("Rule11", ruleBlock);
        rule11.addAntecedent(fis1, "partlyMatch", false);
        rule11.addAntecedent(fis2, "veryMatch", false);
        rule11.addConsequent(result, "match", false);
        ruleBlock.add(rule11);

        Rule rule12 = new Rule("Rule12", ruleBlock);
        rule12.addAntecedent(fis1, "partlyMatch", false);
        rule12.addAntecedent(fis2, "match", false);
        rule12.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule12);

        Rule rule13 = new Rule("Rule13", ruleBlock);
        rule13.addAntecedent(fis1, "partlyMatch", false);
        rule13.addAntecedent(fis2, "partlyMatch", false);
        rule13.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule13);

        Rule rule14 = new Rule("Rule14", ruleBlock);
        rule14.addAntecedent(fis1, "partlyMatch", false);
        rule14.addAntecedent(fis2, "notMatch", false);
        rule14.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule14);

        Rule rule15 = new Rule("Rule15", ruleBlock);
        rule15.addAntecedent(fis1, "partlyMatch", false);
        rule15.addAntecedent(fis2, "veryNotMatch", false);
        rule15.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule15);

        Rule rule16 = new Rule("Rule16", ruleBlock);
        rule16.addAntecedent(fis1, "notMatch", false);
        rule16.addAntecedent(fis2, "veryMatch", false);
        rule16.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule16);

        Rule rule17 = new Rule("Rule17", ruleBlock);
        rule17.addAntecedent(fis1, "notMatch", false);
        rule17.addAntecedent(fis2, "match", false);
        rule17.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule17);

        Rule rule18 = new Rule("Rule18", ruleBlock);
        rule18.addAntecedent(fis1, "notMatch", false);
        rule18.addAntecedent(fis2, "partlyMatch", false);
        rule18.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule18);

        Rule rule19 = new Rule("Rule19", ruleBlock);
        rule19.addAntecedent(fis1, "notMatch", false);
        rule19.addAntecedent(fis2, "notMatch", false);
        rule19.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule19);

        Rule rule20 = new Rule("Rule20", ruleBlock);
        rule20.addAntecedent(fis1, "notMatch", false);
        rule20.addAntecedent(fis2, "veryNotMatch", false);
        rule20.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule20);

        Rule rule21 = new Rule("Rule21", ruleBlock);
        rule21.addAntecedent(fis1, "veryNotMatch", false);
        rule21.addAntecedent(fis2, "veryMatch", false);
        rule21.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule21);

        Rule rule22 = new Rule("Rule22", ruleBlock);
        rule22.addAntecedent(fis1, "veryNotMatch", false);
        rule22.addAntecedent(fis2, "match", false);
        rule22.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule22);

        Rule rule23 = new Rule("Rule23", ruleBlock);
        rule23.addAntecedent(fis1, "veryNotMatch", false);
        rule23.addAntecedent(fis2, "partlyMatch", false);
        rule23.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule23);

        Rule rule24 = new Rule("Rule24", ruleBlock);
        rule24.addAntecedent(fis1, "veryNotMatch", false);
        rule24.addAntecedent(fis2, "notMatch", false);
        rule24.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule24);

        Rule rule25 = new Rule("Rule25", ruleBlock);
        rule25.addAntecedent(fis1, "veryNotMatch", false);
        rule25.addAntecedent(fis2, "veryNotMatch", false);
        rule25.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule25);

        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS s_s_independent(){

        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("speed + slope", functionBlock);

        Variable fis1 = new Variable("fis1"); // speed
        Variable fis2 = new Variable("fis2"); // slope
        Variable result = new Variable("result");

        functionBlock.setVariable(fis1.getName(), fis1);
        functionBlock.setVariable(fis2.getName(), fis2);
        functionBlock.setVariable(result.getName(), result);

        ////define membership functions and linguistic variables
        Value veryNotMatchX[] = {new Value(-0.298), new Value(0), new Value(0.3)};
        Value veryNotMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryNotMatch = new MembershipFunctionPieceWiseLinear(veryNotMatchX, veryNotMatchY);

        Value notMatchX[] = {new Value(0.1), new Value(0.3), new Value(0.5)};
        Value notMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction notMatch = new MembershipFunctionPieceWiseLinear(notMatchX, notMatchY);

        Value partlyMatchX[] = {new Value(0.3), new Value(0.5), new Value(0.7)};
        Value partlyMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction partlyMatch = new MembershipFunctionPieceWiseLinear(partlyMatchX, partlyMatchY);

        Value matchX[] = {new Value(0.5), new Value(0.7), new Value(0.9)};
        Value matchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);

        Value veryMatchX[] = {new Value(0.7), new Value(1), new Value(1.298)};
        Value veryMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryMatch = new MembershipFunctionPieceWiseLinear(veryMatchX, veryMatchY);

        LinguisticTerm ltVeryNotMatch = new LinguisticTerm("veryNotMatch", veryNotMatch);
        LinguisticTerm ltNotMatch = new LinguisticTerm("notMatch", notMatch);
        LinguisticTerm ltPartlyMatch = new LinguisticTerm("partlyMatch", partlyMatch);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        LinguisticTerm ltVeryMatch = new LinguisticTerm("veryMatch", veryMatch);

        fis1.add(ltVeryNotMatch);
        fis1.add(ltNotMatch);
        fis1.add(ltPartlyMatch);
        fis1.add(ltMatch);
        fis1.add(ltVeryMatch);

        fis2.add(ltVeryNotMatch);
        fis2.add(ltNotMatch);
        fis2.add(ltPartlyMatch);
        fis2.add(ltMatch);
        fis2.add(ltVeryMatch);

        result.add(ltVeryNotMatch);
        result.add(ltNotMatch);
        result.add(ltPartlyMatch);
        result.add(ltMatch);
        result.add(ltVeryMatch);

        result.setDefuzzifier(new DefuzzifierCenterOfArea(result));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());

        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(fis1, "veryMatch", false);
        rule1.addAntecedent(fis2, "veryMatch", false);
        rule1.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        rule2.addAntecedent(fis1, "veryMatch", false);
        rule2.addAntecedent(fis2, "match", false);
        rule2.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        rule3.addAntecedent(fis1, "veryMatch", false);
        rule3.addAntecedent(fis2, "partlyMatch", false);
        rule3.addConsequent(result, "match", false);
        ruleBlock.add(rule3);

        Rule rule4 = new Rule("Rule4", ruleBlock);
        rule4.addAntecedent(fis1, "veryMatch", false);
        rule4.addAntecedent(fis2, "notMatch", false);
        rule4.addConsequent(result, "match", false);
        ruleBlock.add(rule4);

        Rule rule5 = new Rule("Rule5", ruleBlock);
        rule5.addAntecedent(fis1, "veryMatch", false);
        rule5.addAntecedent(fis2, "veryNotMatch", false);
        rule5.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule5);

        Rule rule6 = new Rule("Rule6", ruleBlock);
        rule6.addAntecedent(fis1, "match", false);
        rule6.addAntecedent(fis2, "veryMatch", false);
        rule6.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule6);

        Rule rule7 = new Rule("Rule7", ruleBlock);
        rule7.addAntecedent(fis1, "match", false);
        rule7.addAntecedent(fis2, "match", false);
        rule7.addConsequent(result, "match", false);
        ruleBlock.add(rule7);

        Rule rule8 = new Rule("Rule8", ruleBlock);
        rule8.addAntecedent(fis1, "match", false);
        rule8.addAntecedent(fis2, "partlyMatch", false);
        rule8.addConsequent(result, "match", false);
        ruleBlock.add(rule8);

        Rule rule9 = new Rule("Rule9", ruleBlock);
        rule9.addAntecedent(fis1, "match", false);
        rule9.addAntecedent(fis2, "notMatch", false);
        rule9.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule9);

        Rule rule10 = new Rule("Rule10", ruleBlock);
        rule10.addAntecedent(fis1, "match", false);
        rule10.addAntecedent(fis2, "veryNotMatch", false);
        rule10.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule10);

        Rule rule11 = new Rule("Rule11", ruleBlock);
        rule11.addAntecedent(fis1, "partlyMatch", false);
        rule11.addAntecedent(fis2, "veryMatch", false);
        rule11.addConsequent(result, "match", false);
        ruleBlock.add(rule11);

        Rule rule12 = new Rule("Rule12", ruleBlock);
        rule12.addAntecedent(fis1, "partlyMatch", false);
        rule12.addAntecedent(fis2, "match", false);
        rule12.addConsequent(result, "match", false);
        ruleBlock.add(rule12);

        Rule rule13 = new Rule("Rule13", ruleBlock);
        rule13.addAntecedent(fis1, "partlyMatch", false);
        rule13.addAntecedent(fis2, "partlyMatch", false);
        rule13.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule13);

        Rule rule14 = new Rule("Rule14", ruleBlock);
        rule14.addAntecedent(fis1, "partlyMatch", false);
        rule14.addAntecedent(fis2, "notMatch", false);
        rule14.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule14);

        Rule rule15 = new Rule("Rule15", ruleBlock);
        rule15.addAntecedent(fis1, "partlyMatch", false);
        rule15.addAntecedent(fis2, "veryNotMatch", false);
        rule15.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule15);

        Rule rule16 = new Rule("Rule16", ruleBlock);
        rule16.addAntecedent(fis1, "notMatch", false);
        rule16.addAntecedent(fis2, "veryMatch", false);
        rule16.addConsequent(result, "match", false);
        ruleBlock.add(rule16);

        Rule rule17 = new Rule("Rule17", ruleBlock);
        rule17.addAntecedent(fis1, "notMatch", false);
        rule17.addAntecedent(fis2, "match", false);
        rule17.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule17);

        Rule rule18 = new Rule("Rule18", ruleBlock);
        rule18.addAntecedent(fis1, "notMatch", false);
        rule18.addAntecedent(fis2, "partlyMatch", false);
        rule18.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule18);

        Rule rule19 = new Rule("Rule19", ruleBlock);
        rule19.addAntecedent(fis1, "notMatch", false);
        rule19.addAntecedent(fis2, "notMatch", false);
        rule19.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule19);

        Rule rule20 = new Rule("Rule20", ruleBlock);
        rule20.addAntecedent(fis1, "notMatch", false);
        rule20.addAntecedent(fis2, "veryNotMatch", false);
        rule20.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule20);

        Rule rule21 = new Rule("Rule21", ruleBlock);
        rule21.addAntecedent(fis1, "veryNotMatch", false);
        rule21.addAntecedent(fis2, "veryMatch", false);
        rule21.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule21);

        Rule rule22 = new Rule("Rule22", ruleBlock);
        rule22.addAntecedent(fis1, "veryNotMatch", false);
        rule22.addAntecedent(fis2, "match", false);
        rule22.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule22);

        Rule rule23 = new Rule("Rule23", ruleBlock);
        rule23.addAntecedent(fis1, "veryNotMatch", false);
        rule23.addAntecedent(fis2, "partlyMatch", false);
        rule23.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule23);

        Rule rule24 = new Rule("Rule24", ruleBlock);
        rule24.addAntecedent(fis1, "veryNotMatch", false);
        rule24.addAntecedent(fis2, "notMatch", false);
        rule24.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule24);

        Rule rule25 = new Rule("Rule25", ruleBlock);
        rule25.addAntecedent(fis1, "veryNotMatch", false);
        rule25.addAntecedent(fis2, "veryNotMatch", false);
        rule25.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule25);

        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS d_ss_independent(){

        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("speed + slope", functionBlock);

        Variable fis1 = new Variable("fis1"); // distance
        Variable fis2 = new Variable("fis2"); // speed_slope
        Variable result = new Variable("result");

        functionBlock.setVariable(fis1.getName(), fis1);
        functionBlock.setVariable(fis2.getName(), fis2);
        functionBlock.setVariable(result.getName(), result);

        ////define membership functions and linguistic variables
        Value veryNotMatchX[] = {new Value(-0.298), new Value(0), new Value(0.3)};
        Value veryNotMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryNotMatch = new MembershipFunctionPieceWiseLinear(veryNotMatchX, veryNotMatchY);

        Value notMatchX[] = {new Value(0.1), new Value(0.3), new Value(0.5)};
        Value notMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction notMatch = new MembershipFunctionPieceWiseLinear(notMatchX, notMatchY);

        Value partlyMatchX[] = {new Value(0.3), new Value(0.5), new Value(0.7)};
        Value partlyMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction partlyMatch = new MembershipFunctionPieceWiseLinear(partlyMatchX, partlyMatchY);

        Value matchX[] = {new Value(0.5), new Value(0.7), new Value(0.9)};
        Value matchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction match = new MembershipFunctionPieceWiseLinear(matchX, matchY);

        Value veryMatchX[] = {new Value(0.7), new Value(1), new Value(1.298)};
        Value veryMatchY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction veryMatch = new MembershipFunctionPieceWiseLinear(veryMatchX, veryMatchY);

        LinguisticTerm ltVeryNotMatch = new LinguisticTerm("veryNotMatch", veryNotMatch);
        LinguisticTerm ltNotMatch = new LinguisticTerm("notMatch", notMatch);
        LinguisticTerm ltPartlyMatch = new LinguisticTerm("partlyMatch", partlyMatch);
        LinguisticTerm ltMatch = new LinguisticTerm("match", match);
        LinguisticTerm ltVeryMatch = new LinguisticTerm("veryMatch", veryMatch);

        fis1.add(ltVeryNotMatch);
        fis1.add(ltNotMatch);
        fis1.add(ltPartlyMatch);
        fis1.add(ltMatch);
        fis1.add(ltVeryMatch);

        fis2.add(ltVeryNotMatch);
        fis2.add(ltNotMatch);
        fis2.add(ltPartlyMatch);
        fis2.add(ltMatch);
        fis2.add(ltVeryMatch);

        result.add(ltVeryNotMatch);
        result.add(ltNotMatch);
        result.add(ltPartlyMatch);
        result.add(ltMatch);
        result.add(ltVeryMatch);

        result.setDefuzzifier(new DefuzzifierCenterOfArea(result));
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("resultRules");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());

        // define rules
        Rule rule1 = new Rule("Rule1", ruleBlock);
        rule1.addAntecedent(fis1, "veryMatch", false);
        rule1.addAntecedent(fis2, "veryMatch", false);
        rule1.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        rule2.addAntecedent(fis1, "veryMatch", false);
        rule2.addAntecedent(fis2, "match", false);
        rule2.addConsequent(result, "veryMatch", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        rule3.addAntecedent(fis1, "veryMatch", false);
        rule3.addAntecedent(fis2, "partlyMatch", false);
        rule3.addConsequent(result, "match", false);
        ruleBlock.add(rule3);

        Rule rule4 = new Rule("Rule4", ruleBlock);
        rule4.addAntecedent(fis1, "veryMatch", false);
        rule4.addAntecedent(fis2, "notMatch", false);
        rule4.addConsequent(result, "match", false);
        ruleBlock.add(rule4);

        Rule rule5 = new Rule("Rule5", ruleBlock);
        rule5.addAntecedent(fis1, "veryMatch", false);
        rule5.addAntecedent(fis2, "veryNotMatch", false);
        rule5.addConsequent(result, "match", false);
        ruleBlock.add(rule5);

        Rule rule6 = new Rule("Rule6", ruleBlock);
        rule6.addAntecedent(fis1, "match", false);
        rule6.addAntecedent(fis2, "veryMatch", false);
        rule6.addConsequent(result, "match", false);
        ruleBlock.add(rule6);

        Rule rule7 = new Rule("Rule7", ruleBlock);
        rule7.addAntecedent(fis1, "match", false);
        rule7.addAntecedent(fis2, "match", false);
        rule7.addConsequent(result, "match", false);
        ruleBlock.add(rule7);

        Rule rule8 = new Rule("Rule8", ruleBlock);
        rule8.addAntecedent(fis1, "match", false);
        rule8.addAntecedent(fis2, "partlyMatch", false);
        rule8.addConsequent(result, "match", false);
        ruleBlock.add(rule8);

        Rule rule9 = new Rule("Rule9", ruleBlock);
        rule9.addAntecedent(fis1, "match", false);
        rule9.addAntecedent(fis2, "notMatch", false);
        rule9.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule9);

        Rule rule10 = new Rule("Rule10", ruleBlock);
        rule10.addAntecedent(fis1, "match", false);
        rule10.addAntecedent(fis2, "veryNotMatch", false);
        rule10.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule10);

        Rule rule11 = new Rule("Rule11", ruleBlock);
        rule11.addAntecedent(fis1, "partlyMatch", false);
        rule11.addAntecedent(fis2, "veryMatch", false);
        rule11.addConsequent(result, "match", false);
        ruleBlock.add(rule11);

        Rule rule12 = new Rule("Rule12", ruleBlock);
        rule12.addAntecedent(fis1, "partlyMatch", false);
        rule12.addAntecedent(fis2, "match", false);
        rule12.addConsequent(result, "match", false);
        ruleBlock.add(rule12);

        Rule rule13 = new Rule("Rule13", ruleBlock);
        rule13.addAntecedent(fis1, "partlyMatch", false);
        rule13.addAntecedent(fis2, "partlyMatch", false);
        rule13.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule13);

        Rule rule14 = new Rule("Rule14", ruleBlock);
        rule14.addAntecedent(fis1, "partlyMatch", false);
        rule14.addAntecedent(fis2, "notMatch", false);
        rule14.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule14);

        Rule rule15 = new Rule("Rule15", ruleBlock);
        rule15.addAntecedent(fis1, "partlyMatch", false);
        rule15.addAntecedent(fis2, "veryNotMatch", false);
        rule15.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule15);

        Rule rule16 = new Rule("Rule16", ruleBlock);
        rule16.addAntecedent(fis1, "notMatch", false);
        rule16.addAntecedent(fis2, "veryMatch", false);
        rule16.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule16);

        Rule rule17 = new Rule("Rule17", ruleBlock);
        rule17.addAntecedent(fis1, "notMatch", false);
        rule17.addAntecedent(fis2, "match", false);
        rule17.addConsequent(result, "partlyMatch", false);
        ruleBlock.add(rule17);

        Rule rule18 = new Rule("Rule18", ruleBlock);
        rule18.addAntecedent(fis1, "notMatch", false);
        rule18.addAntecedent(fis2, "partlyMatch", false);
        rule18.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule18);

        Rule rule19 = new Rule("Rule19", ruleBlock);
        rule19.addAntecedent(fis1, "notMatch", false);
        rule19.addAntecedent(fis2, "notMatch", false);
        rule19.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule19);

        Rule rule20 = new Rule("Rule20", ruleBlock);
        rule20.addAntecedent(fis1, "notMatch", false);
        rule20.addAntecedent(fis2, "veryNotMatch", false);
        rule20.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule20);

        Rule rule21 = new Rule("Rule21", ruleBlock);
        rule21.addAntecedent(fis1, "veryNotMatch", false);
        rule21.addAntecedent(fis2, "veryMatch", false);
        rule21.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule21);

        Rule rule22 = new Rule("Rule22", ruleBlock);
        rule22.addAntecedent(fis1, "veryNotMatch", false);
        rule22.addAntecedent(fis2, "match", false);
        rule22.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule22);

        Rule rule23 = new Rule("Rule23", ruleBlock);
        rule23.addAntecedent(fis1, "veryNotMatch", false);
        rule23.addAntecedent(fis2, "partlyMatch", false);
        rule23.addConsequent(result, "notMatch", false);
        ruleBlock.add(rule23);

        Rule rule24 = new Rule("Rule24", ruleBlock);
        rule24.addAntecedent(fis1, "veryNotMatch", false);
        rule24.addAntecedent(fis2, "notMatch", false);
        rule24.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule24);

        Rule rule25 = new Rule("Rule25", ruleBlock);
        rule25.addAntecedent(fis1, "veryNotMatch", false);
        rule25.addAntecedent(fis2, "veryNotMatch", false);
        rule25.addConsequent(result, "veryNotMatch", false);
        ruleBlock.add(rule25);

        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        return fis;
    }

    public static FIS test(){

        FIS fis = new FIS();
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("test", functionBlock);


        Variable time = new Variable("time");
        functionBlock.setVariable(time.getName(), time);


        //fuzzify distance
        Value veryLowX[] = {new Value(1800), new Value(5400)};
        Value veryLowY[] = {new Value(1), new Value(0)};
        MembershipFunction veryLow = new MembershipFunctionPieceWiseLinear(veryLowX, veryLowY);

        Value lowX[] = {new Value(1800), new Value(5400), new Value(9000)};
        Value lowY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction low = new MembershipFunctionPieceWiseLinear(lowX, lowY);

        Value mediumX[] = {new Value(5400), new Value(9000), new Value(12600)};
        Value mediumY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction medium = new MembershipFunctionPieceWiseLinear(mediumX, mediumY);

        Value highX[] = {new Value(9000), new Value(12600), new Value(16200)};
        Value highY[] = {new Value(0), new Value(1), new Value(0)};
        MembershipFunction high = new MembershipFunctionPieceWiseLinear(highX, highY);

        Value veryHighX[] = {new Value(12600), new Value(16200)};
        Value veryHighY[] = {new Value(0), new Value(1)};
        MembershipFunction veryHigh = new MembershipFunctionPieceWiseLinear(veryHighX, veryHighY);

        LinguisticTerm ltVeryLow = new LinguisticTerm("veryLow", veryLow);
        LinguisticTerm ltLow = new LinguisticTerm("low", low);
        LinguisticTerm ltMedium = new LinguisticTerm("medium", medium);
        LinguisticTerm ltHigh = new LinguisticTerm("high", high);
        LinguisticTerm ltVeryHigh = new LinguisticTerm("veryHigh", veryHigh);

        time.add(ltVeryLow);
        time.add(ltLow);
        time.add(ltMedium);
        time.add(ltHigh);
        time.add(ltVeryHigh);


        return fis;

    }



    static void animateFis(FIS fis) throws Exception {
        if (JFuzzyChart.UseMockClass) {
            Gpr.debug("Using mock class");
            return; // Nothing done
        }
        JDialogFis jdf = new JDialogFis(fis, 800, 600);
        // Create a plot
        System.out.println(fis.getVariable("result").getValue());
//        jdf.repaint();
    }

    static void animateFis2(FIS fis) throws Exception {
        if (JFuzzyChart.UseMockClass) {
            Gpr.debug("Using mock class");
            return; // Nothing done
        }
        JDialogFis jdf = new JDialogFis(fis, 1200, 500);
        // Create a plot
        System.out.println(fis.getVariable("matching").getValue());
//        jdf.repaint();
    }

    public static void main(String[] args) throws Exception {

        FIS test = distance();
        animateFis2(test);
    }
}