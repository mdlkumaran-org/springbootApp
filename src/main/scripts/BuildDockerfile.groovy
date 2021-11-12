String template = new File("${project.basedir}/src/main/docker/DockerfileTemplate".toString()).getText()

def dockerFileText = new groovy.text.SimpleTemplateEngine().createTemplate(template)
        .make([fileName: project.build.finalName])

println "writing dir " + "${project.basedir}"
new File("${project.basedir}".toString()).mkdirs()

println "writing file"
File dockerFile = new File("${project.basedir}/Dockerfile".toString())

dockerFile.withWriter('UTF-8') { writer ->
    writer.write(dockerFileText)
}