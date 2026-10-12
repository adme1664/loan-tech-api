#!/usr/bin/env bash
set -euo pipefail

if [[ -z "${MONGODB_URI:-}" ]]; then
  echo "Set MONGODB_URI to the MongoDB connection string before running this script." >&2
  exit 1
fi

if [[ ! -t 0 ]]; then
  echo "Refusing to run without an interactive confirmation." >&2
  exit 1
fi

printf 'This will delete every document in database "bigdata" and preserve its collections and indexes.\n'
read -r -p 'Type bigdata to continue: ' confirmation
if [[ "$confirmation" != "bigdata" ]]; then
  echo "Cancelled."
  exit 1
fi

mongosh "$MONGODB_URI" --quiet --eval '
const targetDatabase = db.getSiblingDB("bigdata");
const collections = targetDatabase
  .getCollectionInfos({ type: "collection" })
  .filter(({ name }) => !name.startsWith("system."));

for (const { name } of collections) {
  const result = targetDatabase.getCollection(name).deleteMany({});
  print(`${name}: deleted ${result.deletedCount} document(s)`);
}
'
