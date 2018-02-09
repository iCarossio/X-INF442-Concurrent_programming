import nodes.AnimatedLineSource;
import nodes.Display;
import nodes.LineSource;
import nodes.Node;
import nodes.Processor;
import nodes.Source;
import data.ChannelSelector;
import data.Identity;
import data.MessageQueue;

public class Test3 {

    public static MessageQueue getQueue() {
        // CHANGE AS YOU SOLVE THE EXERCISES
        // return new data.BuiltinQueue(); // COMMENT THIS OUT FIRST
        // return new data.ListQueue(); // EX 2
        // return new data.LockedListQueue(); // EX 2
        // return new data.BoundedQueue(5); // EX 3
       return new data.LockedBoundedQueue(5); // EX 3
        // return new data.AtomicListQueue(); // EX 4
    }

    public static void main(String[] args) {
        // CHANGE AS YOU SOLVE THE EXERCISES
        // processOneImage(); // COMMENT THIS OUT FIRST
        // processTwoImages(); // EX 1
        // test2P1C(); // EX 2, 4
        test3P1C(); // EX 3, 4
        // test3P1C3Displays() // EX 3 complément
    }

    public static void processOneImage() {
        // true means endless sending
        Source source = new LineSource("moon.jpg", "S", true);
        Node display = new Display("D", source.width, source.height);
        source.addConnectionTo(display);
        source.init();
        System.out.println("source started");
    }

    public static void processTwoImages() {
        // true means endless sending
        Source source1 = new LineSource("moon.jpg", "S1", true);
        Node display1 = new Display("D1", source1.width, source1.height);
        source1.addConnectionTo(display1);
        Source source2 = new AnimatedLineSource("brain.gif", "S2");
        Node display2 = new Display("D2", source2.width, source2.height);
        source2.addConnectionTo(display2);
        source1.init();
        System.out.println("source1 started");
        source2.init();
        System.out.println("source2 started");
    }

    // two-producers-one-consumer
    public static void test2P1C() {
        Source source1 = new LineSource("moon.jpg", "S1", false);
        Source source2 = new LineSource("peppers.jpg", "S2", false);
        Node operator = new Processor(getQueue(), new Identity(), "operator");
        source1.addConnectionTo(operator);
        source2.addConnectionTo(operator);
        Node filter1 = new Processor(getQueue(),
                                     new ChannelSelector(source1.channel), "F1");
        Node filter2 = new Processor(getQueue(),
                                     new ChannelSelector(source2.channel), "F2");
        operator.addConnectionTo(filter1);
        operator.addConnectionTo(filter2);
        Node display1 = new Display("D1", source1.width, source1.height);
        Node display2 = new Display("D2", source2.width, source2.height);
        filter1.addConnectionTo(display1);
        filter2.addConnectionTo(display2);
        filter1.init();
        filter2.init();
        operator.init();
        source1.init();
        source2.init();
    }

    // three-producers-one-consumer
    public static void test3P1C() {
        Source source1 = new LineSource("moon.jpg", "S1", true);
        Source source2 = new LineSource("peppers.jpg", "S2", true);
        Source source3 = new LineSource("mandrill.jpg", "S3", true);
        Node operator = new Processor(getQueue(), new Identity(), "operator");
        source1.addConnectionTo(operator);
        source2.addConnectionTo(operator);
        source3.addConnectionTo(operator);
        Node filter1 = new Processor(getQueue(),
                                     new ChannelSelector(source1.channel), "F1");
        Node filter2 = new Processor(getQueue(),
                                     new ChannelSelector(source2.channel), "F2");
        operator.addConnectionTo(filter1);
        operator.addConnectionTo(filter2);
        Node display1 = new Display("D1", source1.width, source1.height);
        Node display2 = new Display("D2", source2.width, source2.height);
        filter1.addConnectionTo(display1);
        filter2.addConnectionTo(display2);
        filter1.init();
        filter2.init();
        operator.init();
        source1.init();
        source2.init();
        source3.init();
    }

    // three-producers-one-consumer
    public static void test3P1C3Displays() {
        Source source1 = new LineSource("moon.jpg", "S1", true);
        Source source2 = new LineSource("peppers.jpg", "S2", true);
        Source source3 = new LineSource("mandrill.jpg", "S3", true);
        Node operator = new Processor(getQueue(), new Identity(), "operator");
        source1.addConnectionTo(operator);
        source2.addConnectionTo(operator);
        source3.addConnectionTo(operator);
        Node filter1 = new Processor(getQueue(),
                                     new ChannelSelector(source1.channel), "F1");
        Node filter2 = new Processor(getQueue(),
                                     new ChannelSelector(source2.channel), "F2");
        Node filter3 = new Processor(getQueue(),
                new ChannelSelector(source3.channel), "F2");
        operator.addConnectionTo(filter1);
        operator.addConnectionTo(filter2);
        operator.addConnectionTo(filter3);
        Node display1 = new Display("D1", source1.width, source1.height);
        Node display2 = new Display("D2", source2.width, source2.height);
        Node display3 = new Display("D2", source2.width, source3.height);
        filter1.addConnectionTo(display1);
        filter2.addConnectionTo(display2);
        filter3.addConnectionTo(display3);
        filter1.init();
        filter2.init();
        filter3.init();
        operator.init();
        source1.init();
        source2.init();
        source3.init();
    }
}
