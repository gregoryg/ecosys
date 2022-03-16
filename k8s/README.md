# Run Tigergraph in EKS/GKE/AKS

## Getting Started

### Prerequisites
    Please ensure the following dependencies are already fulfilled before starting
    - A running ```EKS/GKE/AKS``` cluster
    - The ```kubectl``` command-line tool **(v1.18.0+)**
    - AWS/GCP/Azure account to manage kubernetes resource

### Deployment Steps
   ```bash
   # deploy in EKS
   kubectl apply -k ./eks

   # deploy in GKE
   kubectl apply -k ./gke

   # deploy in AKS
   kubectl apply -k ./aks

   # use tg script with eks in tigergraph namespace 
   ./tg eks create -n tigergraph --size 3 
   ```
### Verify the Tigergraph Status
   ```bash
      kubectl get all -l app=tigergraph
   ```
   Response similar as below :
   ```
   NAME               READY   STATUS    RESTARTS   AGE
   pod/tigergraph-0   1/1     Running   0          6d20h

   NAME                         TYPE           CLUSTER-IP       EXTERNAL-IP                                                              PORT(S)                          AGE
   service/tigergraph-service   LoadBalancer   10.100.214.243   a0ae52e0e62e54bf9b5c07d97deec5e2-982033604.us-east-1.elb.amazonaws.com   9000:30541/TCP,14240:31525/TCP   6d20h
   ```
   Login to the instances
   ```bash
   # use kubectl
   kubectl exec -it tigergraph-0 -- /bin/bash
   # use ssh
   ip_m1=$(kubectl get pod -o wide |grep tigergraph-0| awk '{print $6}')
   ssh tigergraph@ip_m1
   # verify the cluster status
   source ~/.bashrc
   gadmin status -v
   # verify gsql
   gsql ls
   ```
   Try GraphStudio UI, change the url accordingly as upper output ```EXTERNAL-IP``` 
   ```
   http://a0ae52e0e62e54bf9b5c07d97deec5e2-982033604.us-east-1.elb.amazonaws.com:14240
   ```

   Try Tigergraph Rest API, change the url accordingly as upper output ```EXTERNAL-IP```
   ```bash
   curl http://a0ae52e0e62e54bf9b5c07d97deec5e2-982033604.us-east-1.elb.amazonaws.com:9000/echo
   ```
### Kustomize the TG setting
   You can use adjust the kustomize yaml file to change the TG setting. For regular minor changes, strongly recommend to customize them with ```tg``` script as below.
   ```bash
   USAGE:
      $0 K8S_PROVIDER [kustomize|create|delete|list|help] [OPTIONS]
     -n|--namespace :  set namespace to deploy TG cluster  
     -s|--size :       set TG cluster size, default 1
     -v|--version :    set TG cluster version,default as 3.2.0
     -l|--license :    set TG cluster license, default as free tie
     --ha :            set TG cluster ha setting, default 1
     --pv :            set Persistent volume size, default as 50
     --prefix :        set Pod name with prefix
   
   # Examples when working in eks:
   ## Generate the manifest for deployment
     ./tg eks kustomize -n tigergraph --size 3 --ha 3
   ## Create TG cluster:
     ./tg eks create -n tigergraph -s 2 
   ## Delete TG cluster: 
     ./tg eks delete
   ## List TG cluster:
     ./tg eks list -n tigergraph
   ```
    
