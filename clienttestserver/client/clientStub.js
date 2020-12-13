const PROTO_PATH = "../Communication.proto";


const grpc = require("grpc");
const protoLoader = require("@grpc/proto-loader");

var packageDefinition = protoLoader.loadSync(PROTO_PATH, {
    keepCase: true,
    longs: String,
    enums: String,
    arrays: true
});

const protoDescriptor = grpc.loadPackageDefinition(packageDefinition);

const clientStub = new protoDescriptor.testservergrpc.Communication(
    "localhost:9090",
    grpc.credentials.createInsecure()
);

module.exports = clientStub;
