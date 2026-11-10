# Talend Component Suite tJSONDoc*
This project consists of components to work with JSON in a fine grained way.
* tJSONDocOpen - creates or opens a JSON document (this is always the start point and parse every String content into a native JSON document. All other components use this component and reading or writing into this document)
* tJSONDocOutput - create sub-nodes to the document or write into fields
* tJSONDocInput - select nodes via json-path and read attributes from them
* tJSONDocExtractFields - similiar to tJSONDocInput but can work within a flow and do not have to start the flow
* tJSONDocInputStream - reads a large JSON file and use the streaming API to extract values or objects
* tJSONDocSave - write the JSON document as file or provide the content as flow
* tJSONDocDiff - compares 2 JSON documents and returns a detailed list of the differences
* tJSONDocMerge - merges 2 JSON documents by the help of key attributes
* tJSONDocTraverseFields - returns all key value pair for simple attributes, retrieves through the whole object and array hierachy

These components can be donwloaded in the Release section of this repository (at the right side).

Please refer to the [documentation](https://github.com/jlolling/talendcomp_tJSONDoc/blob/master/doc/tJSONDoc.pdf)
