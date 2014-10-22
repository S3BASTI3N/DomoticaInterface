package net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects;

/**
 * Created by sebastien on 22-10-14.
 */
public class LogicRule {

    public int id_;
    public String name_;
    public LogicRuleStateDescriber condition_;
    public LogicRuleStateDescriber action_;

    public static final int STATE_ON  = 1;
    public static final int STATE_OFF = 0;


    public LogicRule( int id, String name, LogicRuleStateDescriber condition, LogicRuleStateDescriber action ) {

        id_ = id;
        name_ = name;
        condition_ = condition;
        action_ = action;

    }

    public String toString() {
        return "Logic rule id(" + id_ + ")  name(" + name_ + ")  condition("+condition_+")  action(" +action_;
    }
}
