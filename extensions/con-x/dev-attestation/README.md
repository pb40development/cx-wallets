## How to use

This extension allows you to freely define the contents of a credentialSubject field for a Verifiable Credential. 

It is not intended to be used in a productive scenario. It's intended purpose is to help the construct-x project to 
easily define new types of Verifiable Credentials and test them without any hard-coded restrictions. 

A possible request body for creating an attestation on your wallet may look like this: 

```json
{
  "attestationType": "dev",
  "id": "dev-def-1",
  "configuration": {
    "did:web:consumer-idhub:user:consumer": {
      "isConsumer": true,
      "isProvider": false,
      "foo": {
        "bar": 123
      }
    },
    "did:web:provider-idhub:user:provider": {
      "isConsumer": false,
      "isProvider": true,
      "foo": {
        "bar": 789
      }
    },
    "default": {
      "isConsumer": false,
      "isProvider": false,
      "foo": {
        "bar": 0
      }
    },
    "blackList": []
  }
}
```

The key `attestationType` field must be set to `dev` (or else this extension will not be called at runtime). 

The key `id` must be a unique string, that you can set freely. You will need to reference to this `id` later (see below). 

The `configuration` key must be a JSON non-empty object, that may hold several other key-value pairs. 

That `configuration`-object may contain a `blackList` key, which (if it exists) must be of type list. This list must 
contain nothing else than string-typed entries. All of these entries must be valid did:web ids. 

The `configuration`-object may contain a `default` key, which (if it exists) must be of type object. That object must not 
be empty. It neeeds to contain at least one key-value-pair (not counting an `id` key-value-pair) Beyond that, the 
content of that object can be freely designed by you. It will be used to define the credentialSubject of Verifiable Credentials. 

Beyond that, the `configuration`-object may contain an arbitrary number of additional keys. These keys must be strings 
in the form of did:web id's. The value must be a non-empty object. The same restrictions as described for the `default` 
apply here. 

Note: the credentialSubject will always contain an `id` key, whose value will be automatically set to the did:web id of 
the requesting holder. So you don't need (and normally should not) set an `id` key manually when you are designing a 
credentialSubject using the above-mentioned means. 

The attestations that are created in this way, can be referenced in a subsequent request to create a Credential Type definition. 
Please also see this [documentation](./../../../docs/developer/architecture/issuer/issuance/issuance.process.md). 

A possible request body may look like this: 

```json
{
  "attestations": [
    "dev-def-1"
  ],
  "credentialType": "MembershipCredential",
  "id": "dev-mem-def",
  "jsonSchema": "{}",
  "jsonSchemaUrl": "https://example.com/schema/dev-credential.json",
  "rules": [],
  "format": "VC1_0_JWT",
  "mappings": [
    {
      "input": "content",
      "output": "credentialSubject",
      "required": true
    }
  ],
  "validity": 15552000
}
```

In the array of the `attestations` field, you need to use the same `id` that you used during the attestation creation request (see above). 

The `credentialType` can be set to your required credential type, like e.g. `FooCredential`. 

The `jsonSchema`, `jsonSchemaUrl`, `rules` and `format` fields should remain as shown above. (Change them at your own risk) 

The `mappings` field must remain exactly as shown above, or otherwise this extension will be unable to work. 

The `validity` field defines the timespan, after which a credential of this type will expire in seconds (15552000 seconds = 180 days). You 
can change this to any positive value you like. 


