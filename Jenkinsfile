podTemplate(
    name: 'deltacare',
    label: 'deltacare',
    namespace: 'ops',
    cloud: 'kubernetes',
    containers: [
        containerTemplate(name: 'docker',image: 'docker',command: '/bin/sh -c',args: 'cat',ttyEnabled: true,workingDir: '/home/jenkins/agent'),
        containerTemplate(name: 'helm',image: 'dtzar/helm-kubectl:3.4.1',command: '/bin/sh -c',args: 'cat',ttyEnabled: true,workingDir: '/home/jenkins/agent'),
        containerTemplate(name: 'maven',image: 'maven:3.6.3-amazoncorretto-11',command: '/bin/sh -c',args: 'cat',ttyEnabled: true,workingDir: '/home/jenkins/agent')
    ],
    volumes: [
        hostPathVolume(hostPath: '/var/run/docker.sock',mountPath: '/var/run/docker.sock'),
        persistentVolumeClaim(mountPath: '/home/jenkins/.m2/', claimName: 'maven-repo', readOnly: false)
    ]
)
{
    node('deltacare') {

        def APP_NAME='api-empresa'
        def APP_PROFILE='prd'
        def APP_VERSION
        def URL_REPO_GIT="https://github.com/delta-care/${APP_NAME}.git"
        def URL_REPO_CHART="http://deltacare-chartmuseum:8080"
        def URL_REPO_HPUSH="https://github.com/chartmuseum/helm-push.git"
        def IMAGE_NAME_DOCKER="deltacare/${APP_NAME}"
        def IMAGE_NAME_CHART="deltacare/${APP_NAME}"
        def K8S_NAMESPACE='prd'
        def OBJ_REPO_GIT
        
        stage('Checkout') {
            OBJ_REPO_GIT = git branch: 'main', credentialsId: 'github', url: URL_REPO_GIT
            def props = readMavenPom file: 'pom.xml'
            APP_VERSION = props.version
        }
        
        stage('Build') {
            container('maven') {
                sh 'mvn clean package -D skipTests=true'
            }
        }
        
        stage('Unit Test') {
            container('maven') {
                sh 'mvn test'
            }
        }

        stage('Code Analysis') {
            container('maven') {
                withSonarQubeEnv(installationName: 'SonarCloudServer') {
                    sh 'mvn jacoco:report org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -P sonar'
                }
            }
            timeout(time: 1, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: true
            }
        }
        
        stage('Release') {
            container('docker') {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'DOCKER_HUB_PASS', usernameVariable: 'DOCKER_HUB_USER')]) {
                    sh "docker build -t ${IMAGE_NAME_DOCKER}:${APP_VERSION} ."
                    sh "docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASS}"
                    sh "docker push ${IMAGE_NAME_DOCKER}:${APP_VERSION}"
                }
            }
        }
        
        stage('Deploy DEV') {
            container('helm') {
                sh "sed -i 's/^appVersion:.*\$/appVersion: ${APP_VERSION}/' ./helm/Chart.yaml"
                //sh "helm uninstall ${APP_NAME} --namespace ${K8S_NAMESPACE}"
                sh "helm upgrade ${APP_NAME} ./helm --install --namespace ${K8S_NAMESPACE} --set app.profile=${APP_PROFILE} --set image.tag=${APP_VERSION} --set k8s.namespace=${K8S_NAMESPACE}"
                sh "helm repo add deltacare ${URL_REPO_CHART}"
                sh "helm plugin install ${URL_REPO_HPUSH}"
                sh "helm push helm/ deltacare"
                sh "helm repo update"
            }
        }
    }
}
