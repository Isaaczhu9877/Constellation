import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Size;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableNode;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.*;

import java.io.File;
import java.io.IOException;

public final class DSLVisualizer
{
    private int overallRunTime;

    public DSLVisualizer(MethodNode root, int overallRunTime) throws IOException {
        this.overallRunTime = overallRunTime;
        Graphviz.fromGraph(createMutableGraph(root, false)).render(Format.SVG_STANDALONE).toFile(new File("graph/x.svg"));
        Graphviz.fromGraph(createMutableGraph(root, true)).render(Format.SVG_STANDALONE).toFile(new File("graph/x_agg.svg"));
    }

    private guru.nidi.graphviz.model.Graph createMutableGraph(MethodNode root, boolean isAggregate) {
        return graph("DSL Visualizer")
                .directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .with(createNodes(root, isAggregate));
    }

    private MutableNode createNodes(MethodNode methodNode, boolean isAggregate) {
        MutableNode mutableNode = mutNode(methodNode.getName());
        applyNodeStyles(mutableNode, methodNode, isAggregate);
        methodNode.children.forEach(c -> {
            mutableNode.addLink(Link
                    .to(createNodes(c, isAggregate))
                    .with("label", c.frequency)
                    .with(Font.size(getFontSize(c, isAggregate))));
        });
        return mutableNode;
    }

    private int getFontSize(MethodNode node, boolean isAggregate) {
        double nodePercent = node.duration / this.overallRunTime;
        double nodeDimension = nodePercent + 1;
        if (isAggregate) {
            nodeDimension *= node.frequency;
        }
        return (int) nodeDimension * 10 + 5;
    }

    private void applyNodeStyles(MutableNode mutableNode, MethodNode methodNode, boolean isAggregate) {
        double nodePercent = methodNode.duration * 1.0 / this.overallRunTime;
        double nodeDimension = nodePercent + 1;
        nodePercent *= 100;
        if (isAggregate) {
            nodeDimension *= methodNode.frequency;
            nodePercent *= methodNode.frequency;
        }
        System.out.println(nodeDimension);
        mutableNode.setName(String.format("%s\n%.0f%%", mutableNode.name(), nodePercent));
        mutableNode.add(Size.std().size(nodeDimension, nodeDimension), Font.size(getFontSize(methodNode, isAggregate)));
    }
}

