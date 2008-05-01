package hudson.plugins.findbugs;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.plugins.findbugs.parser.FindBugsCollector;
import hudson.plugins.findbugs.util.HealthAwarePublisher;
import hudson.plugins.findbugs.util.HealthReportBuilder;
import hudson.plugins.findbugs.util.PluginDescriptor;
import hudson.plugins.findbugs.util.model.JavaProject;
import hudson.tasks.Publisher;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

/**
 * Publishes the results of the FindBugs analysis.
 *
 * @author Ulli Hafner
 */
public class FindBugsPublisher extends HealthAwarePublisher {
    /** Default FindBugs pattern. */
    private static final String DEFAULT_PATTERN = "**/findbugs.xml";
    /** Descriptor of this publisher. */
    public static final PluginDescriptor FIND_BUGS_DESCRIPTOR = new FindBugsDescriptor();

    /**
     * Creates a new instance of <code>FindBugsPublisher</code>.
     *
     * @param pattern
     *            Ant file-set pattern to scan for FindBugs files
     * @param threshold
     *            Bug threshold to be reached if a build should be considered as
     *            unstable.
     * @param healthy
     *            Report health as 100% when the number of warnings is less than
     *            this value
     * @param unHealthy
     *            Report health as 0% when the number of warnings is greater
     *            than this value
     * @param height
     *            the height of the trend graph
     * @stapler-constructor
     */
    public FindBugsPublisher(final String pattern, final String threshold, final String healthy, final String unHealthy, final String height) {
        super(pattern, threshold, healthy, unHealthy, height);
    }

    /** {@inheritDoc} */
    @Override
    public Action getProjectAction(final AbstractProject<?, ?> project) {
        return new FindBugsProjectAction(project, getTrendHeight());
    }

    /** {@inheritDoc} */
    @Override
    public JavaProject perform(final AbstractBuild<?, ?> build, final BuildListener listener) throws InterruptedException, IOException {
        PrintStream logger = listener.getLogger();
        logger.println("Collecting findbugs analysis files...");

        JavaProject project = parseAllWorkspaceFiles(build, logger);
        FindBugsResult result = new FindBugsResultBuilder().build(build, project);

        HealthReportBuilder healthReportBuilder = createHealthReporter(
                Messages.FindBugs_ResultAction_HealthReportSingleItem(),
                Messages.FindBugs_ResultAction_HealthReportMultipleItem("%d"));
        build.getActions().add(new FindBugsResultAction(build, result, healthReportBuilder));

        return project;
    }

    /**
     * Scans the workspace for FindBugs files matching the specified pattern and
     * returns all found annotations merged in a project.
     *
     * @param build
     *            the build to create the action for
     * @param logger
     *            the logger
     * @return the project with the annotations
     * @throws IOException
     *             if the files could not be read
     * @throws InterruptedException
     *             if user cancels the operation
     */
    private JavaProject parseAllWorkspaceFiles(final AbstractBuild<?, ?> build, final PrintStream logger) throws IOException, InterruptedException {
        FindBugsCollector collector = new FindBugsCollector(
                    logger,
                    build.getTimestamp().getTimeInMillis(),
                    StringUtils.defaultIfEmpty(getPattern(), DEFAULT_PATTERN),
                    true);

        return build.getProject().getWorkspace().act(collector);
    }

    /** {@inheritDoc} */
    public Descriptor<Publisher> getDescriptor() {
        return FIND_BUGS_DESCRIPTOR;
    }
}
