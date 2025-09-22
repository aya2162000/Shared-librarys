def vulnerability(String imageName) {
    sh '''
        echo image - ${imageName}

        trivy image ${imageName} \
            --severity LOW,MEDIUM,HIGH \
            --exit-code 0 \
            --quiet \
            --format json -o trivy-image-MEDIUM-results.json

        trivy image ${imageName} \
            --severity CRITICAL \
            --exit-code 1 \
            --quiet \
            --format json -o trivy-image-CRITICAL-results.json
    '''
}

def reportsConverter() {
    sh '''
        trivy convert \
            --format template --template "@/usr/local/share/trivy/templates/html.tpl" \
            --output trivy-image-MEDIUM-results.html trivy-image-MEDIUM-results.json

        trivy convert \
            --format template --template "@/usr/local/share/trivy/templates/html.tpl" \
            --output trivy-image-CRITICAL-results.html trivy-image-CRITICAL-results.json

        trivy convert \
            --format template --template "@/usr/local/share/trivy/templates/junit.tpl" \
            --output trivy-image-MEDIUM-results.xml trivy-image-MEDIUM-results.json

        trivy convert \
            --format template --template "@/usr/local/share/trivy/templates/junit.tpl" \
            --output trivy-image-CRITICAL-results.xml trivy-image-CRITICAL-results.json
    '''
}
