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
        hostPathVolume(hostPath: '/var/run/docker.sock',mountPath: '/var/run/docker.sock')
    ]
)
{
    node('deltacare') {

        def APP_NAME='api-empresa'
        def APP_VERSION
        def URL_REPO_GIT="https://github.com/delta-care/${APP_NAME}.git"
        def URL_REPO_CHART="http://deltacare-chartmuseum:8080"
        def URL_REPO_HPUSH="https://github.com/chartmuseum/helm-push.git"
        def IMAGE_NAME_DOCKER="deltacare/${APP_NAME}"
        def IMAGE_NAME_CHART="deltacare/${APP_NAME}"
        def K8S_NAMESPACE='dev'
        def DOMAIN='deltacare.xyz'
        def SUBDOMAIN='dev'
        def OBJ_REPO_GIT

        stage('Checkout') {
            OBJ_REPO_GIT = git branch: 'main', credentialsId: 'dockerhub-jdscio', url: URL_REPO_GIT
            def props = readMavenPom file: 'pom.xml'
            APP_VERSION = props.version
        }

        stage('Unit Test') {
            container('maven') {
                sh 'mvn test'
            }
        }

        stage('Package') {
            container('maven') {
                sh 'mvn package -DskipTests'
            }
            container('docker') {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-jdscio', passwordVariable: 'DOCKER_HUB_PASS', usernameVariable: 'DOCKER_HUB_USER')]) {
                    sh "docker build -t ${IMAGE_NAME_DOCKER}:${APP_VERSION} ."
                    sh "docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASS}"
                    sh "docker push ${IMAGE_NAME_DOCKER}:${APP_VERSION}"
                }
            }
        }

        /*
        stage('Deploy') {
            container('helm') {
                sh "sed -i 's/^appVersion:.*\$/appVersion: ${APP_VERSION}/' ./helm/Chart.yaml"
                sh "helm upgrade ${APP_NAME} ./helm --install --set image.tag=${APP_VERSION} --namespace ${K8S_NAMESPACE} --set ingress.hosts[0].host=${SUBDOMAIN}.${DOMAIN} --set ingress.hosts[0].paths[0].path=/"
                sh "helm repo add deltacare ${URL_REPO_CHART}"
                sh "helm plugin install ${URL_REPO_HPUSH}"
                sh "helm push helm/ deltacare"
                sh "helm repo update"
            }
        }*/
    }
}