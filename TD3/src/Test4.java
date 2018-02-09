import java.util.LinkedList;
import java.util.List;

import nodes.Display;
import nodes.LineSource;
import nodes.Node;
import nodes.PixelSource;
import nodes.Processor;
import nodes.ProcessorN;
import nodes.ProcessorPool;
import nodes.Source;
import nodes.TileSource;
import data.ChannelSelector;
import data.ColorSelector;
import data.Identity;
import data.LineDistorter;
import data.MessageProcessor;
import data.MessageQueue;
import data.PixelDistorter;
import data.TileDistorter;

@SuppressWarnings("unused")
public class Test4 {

    public static MessageQueue getQueue(int max) {
        // CHANGE AS YOU SOLVE THE EXERCISES
        // return new data.BuiltinQueue(max); // COMMENT THIS OUT FIRST
        return new data.BlockingBoundedQueue(max); // EX 1 -- 5
        // return new data.AtomicBoundedQueue(max); // EX 6
    }

    public static void main(String[] args) {
        toStart.clear();
        // BEGIN modifiable bits
        // setupSplit(); // EX 1 (alternative 1)
        // setupDistort(); // EX 1 (alternative 2)
        // setupMultiDistort1(); // EX 1 (alternative 3)
        // setupMultiDistortN(10); // EX 2
        //setupMultiDistortPool(10); // EX 3
        // setupDistortFrame(); // EX 4
        setupDistortAnimation(); // EX 4
        // END modifiable bits
        for (Node n : toStart) {
            n.init();
            System.out.format("Node \"%s\" started.\n", n.getName());
        }
        toStart.clear();
    }

    // a list of nodes to start
    private static List<Node> toStart = new LinkedList<>();

    // adds the node to the toStart list and then returns it
    private static <T extends Node> T start(T node) {
        toStart.add(node);
        return node;
    }

    private static void setupSplit() {
        Source src = start(new LineSource("houses.jpg", "src", false));
        Node split = start(new Processor(getQueue(), new Identity(), "split"));
        Node rSel = start(new Processor(getQueue(), ColorSelector.red, "rSel"));
        Node gSel = start(new Processor(getQueue(), ColorSelector.green, "gSel"));
        Node bSel = start(new Processor(getQueue(), ColorSelector.blue, "bSel"));
        Node rDis = new Display("rDis", src.width, src.height);
        Node gDis = new Display("gDis", src.width, src.height);
        Node bDis = new Display("bDis", src.width, src.height);

        src.addConnectionTo(split);
        split.addConnectionTo(rSel);
        split.addConnectionTo(gSel);
        split.addConnectionTo(bSel);
        rSel.addConnectionTo(rDis);
        gSel.addConnectionTo(gDis);
        bSel.addConnectionTo(bDis);
    }

    private static Node makeDistortion(Source src, PixelDistorter pd,
                                       String name) {
        Node procNode = start(new Processor(getQueue(), pd, "|" + name + "|"));
        Node disNode = new Display("|D" + name, src.width, src.height);
        src.addConnectionTo(procNode);
        procNode.addConnectionTo(disNode);
        return procNode;
    }

    private static void setupDistort() {
        Source src = start(new PixelSource("daffodilsmall.jpg", "src", false));
        makeDistortion(src, PixelDistorter.identity, "id");
        makeDistortion(src, PixelDistorter.luminosity, "luminosity");
        makeDistortion(src, PixelDistorter.gaussianBlur, "blur");
        makeDistortion(src, PixelDistorter.sharpen, "sharp");
        makeDistortion(src, PixelDistorter.emboss, "emboss");
        makeDistortion(src, PixelDistorter.edgeDetect, "edge");
    }

    private static Node selectAndDisplay(Source src) {
        String channel = src.channel;
        ChannelSelector sel = new ChannelSelector(channel);
        Node selNode = start(new Processor(getQueue(), sel, channel + "[sel]"));
        Node disNode = new Display(channel + "[dis]", src.width, src.height);
        selNode.addConnectionTo(disNode);
        return selNode;
    }

    private static void setupMultiDistort(Processor proc) {
        String[] imgs = {"daffodilsmall.jpg", "mandrill.jpg", "peppers.jpg",
                         "moon.jpg", "houses.jpg"};
        List<Source> srcs = new LinkedList<>();
        for (String img : imgs) {
            srcs.add(start(new PixelSource(img, img, false)));
        }

        start(proc);
        for (Source src : srcs) {
            src.addConnectionTo(proc);
            proc.addConnectionTo(selectAndDisplay(src));
        }
    }

    private static void setupMultiDistort1() {
        Processor proc = new Processor(getQueue(), PixelDistorter.edgeDetect,
                                       "proc");
        setupMultiDistort(proc);
    }

    private static void setupMultiDistortN(int numThreads) {
        Processor proc = new ProcessorN(numThreads, getQueue(),
                                        PixelDistorter.edgeDetect, "proc");
        setupMultiDistort(proc);
    }

    private static void setupMultiDistortPool(int concur) {
        Processor proc = new ProcessorPool(concur, getQueue(),
                                           PixelDistorter.edgeDetect, "proc");
        setupMultiDistort(proc);
    }

    private static void setupDistortFrame() {
        Source s = start(new TileSource("mandrill.jpg", "S", true));
        MessageProcessor mp = new TileDistorter(10, PixelDistorter.emboss);
        Processor p = start(new Processor(getQueue(), mp, "proc"));
        Display d = new Display("D", s.width, s.height);
        s.addConnectionTo(p);
        p.addConnectionTo(d);
    }

    private static void setupDistortAnimation() {
        Source s = start(new TileSource("brain.gif", "S", true));
        MessageProcessor mp = new TileDistorter(10, PixelDistorter.emboss);
        Processor p = start(new Processor(getQueue(), mp, "proc"));
        Display d = new Display("D", s.width, s.height);
        s.addConnectionTo(p);
        p.addConnectionTo(d);
    }

    private static final int DEFAULT_SIZE = 500;

    public static MessageQueue getQueue() {
        return getQueue(DEFAULT_SIZE);
    }

}
